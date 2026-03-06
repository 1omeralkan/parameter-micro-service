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

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryDto> getAllActiveCountries() {
        log.info("Sistemdeki tüm aktif ülkeler getiriliyor...");

        List<CountryEntity> activeCountries = countryRepository.findAllByIsActiveTrue();

        return activeCountries.stream()
                .map(countryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CountryDto getCountryByIsoCode(String isoCode) {
        log.info("{} ISO kodlu aktif ülke aranıyor...", isoCode);

        CountryEntity country = countryRepository.findByIsoCodeAndIsActiveTrue(isoCode)
                .orElseThrow(() -> new RuntimeException("Aktif ülke bulunamadı! ISO Kod: " + isoCode));

        return countryMapper.toDto(country);
    }

    @Override
    public CountryDto createCountry(CountryCreateDto createDto) {
        log.info("{} ISO kodlu yeni ülke ekleniyor...", createDto.isoCode());

        // Aynı ISO koduna sahip aktif bir ülke var mı kontrol et!
        Optional<CountryEntity> existingCountry = countryRepository.findByIsoCodeAndIsActiveTrue(createDto.isoCode());
        if (existingCountry.isPresent()) {
            throw new RuntimeException("Bu ISO koduna sahip aktif bir ülke zaten var: " + createDto.isoCode());
        }

        // DTO'yu Entity'ye çevir
        CountryEntity newEntity = countryMapper.toEntity(createDto);

        // Veritabanına kaydet
        CountryEntity savedEntity = countryRepository.save(newEntity);

        // Kaydedilen Entity'i dış dünyaya dönmek için tekrar DTO'ya çevir
        return countryMapper.toDto(savedEntity);
    }

    @Override
    public void deleteCountry(Long id) {
        log.info("{} ID'li ülke pasife çekiliyor...", id);

        CountryEntity country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinmek istenen ülke bulunamadı! ID: " + id));

        if (!country.getIsActive()) {
            throw new RuntimeException("Bu ülke zaten pasif durumda! ID: " + id);
        }

        country.setIsActive(false);
        countryRepository.save(country);

        log.info("{} ID'li ülke başarıyla pasife çekildi.", id);
    }

    @Override
    public CountryDto updateCountry(Long id, CountryUpdateDto updateDto) {
        log.info("{} ID'li ülke güncelleniyor...", id);

        CountryEntity country = countryRepository.findById(id)
                .filter(CountryEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Güncellenecek aktif ülke bulunamadı! ID: " + id));

        if (!country.getIsoCode().equalsIgnoreCase(updateDto.isoCode())) {
            Optional<CountryEntity> existingWithNewCode = countryRepository.findByIsoCodeAndIsActiveTrue(updateDto.isoCode());
            if (existingWithNewCode.isPresent()) {
                throw new RuntimeException("Bu ISO koduna sahip başka bir ülke zaten var: " + updateDto.isoCode());
            }
        }

        countryMapper.updateEntityFromDto(updateDto, country);

        CountryEntity updatedCountry = countryRepository.save(country);

        return countryMapper.toDto(updatedCountry);
    }
    @Override
    public CountryDto getCountryById(Long id) {
        log.info("{} ID'li aktif ülke aranıyor...", id);
        CountryEntity country = countryRepository.findById(id)
                .filter(CountryEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Aktif ülke bulunamadı! ID: " + id));
        return countryMapper.toDto(country);
    }
}