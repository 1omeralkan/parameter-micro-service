package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.entity.CountryEntity;
import com.omeralkan.parameter.mapper.CountryMapper;
import com.omeralkan.parameter.repository.CountryRepository;
import com.omeralkan.parameter.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
}