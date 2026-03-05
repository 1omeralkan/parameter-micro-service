package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;
import com.omeralkan.parameter.entity.CountryEntity;
import com.omeralkan.parameter.mapper.CountryMapper;
import com.omeralkan.parameter.repository.CountryRepository;
import com.omeralkan.parameter.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j // Loglama yapmak için
@Service
@RequiredArgsConstructor // Lombok sayesinde Constructor Injection (Bağımlılık Enjeksiyonu) otomatik yapılır
public class CountryServiceImpl implements CountryService {

    // Hardcoded bağımlılık yok! Spring bunları constructor üzerinden bize enjekte edecek [cite: 2026-03-01]
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryDto> getAllActiveCountries() {
        log.info("Sistemdeki tüm aktif ülkeler getiriliyor...");

        List<CountryEntity> activeCountries = countryRepository.findAllByIsActiveTrue();

        // Gelen Entity listesini, Java Stream API kullanarak DTO listesine çeviriyoruz
        return activeCountries.stream()
                .map(countryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CountryDto getCountryByIsoCode(String isoCode) {
        log.info("{} ISO kodlu aktif ülke aranıyor...", isoCode);

        // Eğer veritabanında bulamazsa, hata fırlat (Exception Handling)
        CountryEntity country = countryRepository.findByIsoCodeAndIsActiveTrue(isoCode)
                .orElseThrow(() -> new RuntimeException("Aktif ülke bulunamadı! ISO Kod: " + isoCode));

        return countryMapper.toDto(country);
    }

    @Override
    public CountryDto createCountry(CountryCreateDto createDto) {
        log.info("{} ISO kodlu yeni ülke ekleniyor...", createDto.isoCode());

        // İŞ KURALI: Aynı ISO koduna sahip aktif bir ülke var mı kontrol et!
        Optional<CountryEntity> existingCountry = countryRepository.findByIsoCodeAndIsActiveTrue(createDto.isoCode());
        if (existingCountry.isPresent()) {
            throw new RuntimeException("Bu ISO koduna sahip aktif bir ülke zaten var: " + createDto.isoCode());
        }

        // 1. DTO'yu Entity'ye çevir
        CountryEntity newEntity = countryMapper.toEntity(createDto);

        // 2. Veritabanına kaydet (JPA bize ID'si dolmuş taze entity'i geri döner)
        CountryEntity savedEntity = countryRepository.save(newEntity);

        // 3. Kaydedilen Entity'i dış dünyaya dönmek için tekrar DTO'ya çevir
        return countryMapper.toDto(savedEntity);
    }

    @Override
    public void deleteCountry(Long id) {
        log.info("{} ID'li ülke pasife çekiliyor...", id);

        // 1. Ülkeyi bul
        CountryEntity country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinmek istenen ülke bulunamadı! ID: " + id));

        // 2. İŞ KURALI: Ülke zaten pasifse sistemi yormayalım, uyaralım
        if (!country.getIsActive()) {
            throw new RuntimeException("Bu ülke zaten pasif durumda! ID: " + id);
        }

        // 3. Gerçekten SİLMİYORUZ! Sadece şalteri indiriyoruz (Soft Delete)
        country.setIsActive(false);
        countryRepository.save(country);

        log.info("{} ID'li ülke başarıyla pasife çekildi.", id);
    }

    @Override
    public CountryDto updateCountry(Long id, CountryUpdateDto updateDto) {
        log.info("{} ID'li ülke güncelleniyor...", id);

        // 1. Önce ülkeyi bul (Eğer yoksa veya pasifse hata fırlat)
        CountryEntity country = countryRepository.findById(id)
                .filter(CountryEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Güncellenecek aktif ülke bulunamadı! ID: " + id));

        // 2. İŞ KURALI (Kritik): Eğer ISO kodunu değiştirmek istiyorsa, bu yeni kod başka bir ülkede var mı?
        if (!country.getIsoCode().equalsIgnoreCase(updateDto.isoCode())) {
            Optional<CountryEntity> existingWithNewCode = countryRepository.findByIsoCodeAndIsActiveTrue(updateDto.isoCode());
            if (existingWithNewCode.isPresent()) {
                throw new RuntimeException("Bu ISO koduna sahip başka bir ülke zaten var: " + updateDto.isoCode());
            }
        }

        // 3. Entity'nin içini yeni verilerle doldur
        countryMapper.updateEntityFromDto(updateDto, country);

        // 4. Veritabanına kaydet (Hibernate bunun mevcut bir kayıt olduğunu ID'sinden anlar ve UPDATE sorgusu atar)
        CountryEntity updatedCountry = countryRepository.save(country);

        return countryMapper.toDto(updatedCountry);
    }
}