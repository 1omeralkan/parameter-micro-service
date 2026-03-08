package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;
import com.omeralkan.parameter.entity.CountryEntity;
import com.omeralkan.parameter.exception.BusinessException;
import com.omeralkan.parameter.mapper.CountryMapper;
import com.omeralkan.parameter.repository.CountryRepository;
import com.omeralkan.parameter.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    // SONARQUBE: Tekrarlanan metinleri sabit değişkene aldık
    private static final String COUNTRY_NOT_FOUND = "COUNTRY-404";
    private static final String COUNTRY_ALREADY_EXISTS = "COUNTRY-409";
    private static final String PARAM_BAD_REQUEST = "PARAM-400";

    @Override
    public List<CountryDto> getAllActiveCountries() {
        log.info("Sistemdeki tüm aktif ülkeler getiriliyor...");

        List<CountryEntity> activeCountries = countryRepository.findAllByIsActiveTrue();

        return activeCountries.stream()
                .map(countryMapper::toDto)
                .toList(); // SONARQUBE: Collectors yerine toList() kullanıldı
    }

    @Override
    public CountryDto getCountryByIsoCode(String isoCode) {
        log.info("{} ISO kodlu aktif ülke aranıyor...", isoCode);

        CountryEntity country = countryRepository.findByIsoCodeAndIsActiveTrue(isoCode)
                .orElseThrow(() -> new BusinessException(COUNTRY_NOT_FOUND));

        return countryMapper.toDto(country);
    }

    @Override
    public CountryDto createCountry(CountryCreateDto createDto) {
        log.info("{} ISO kodlu yeni ülke ekleniyor...", createDto.isoCode());

        Optional<CountryEntity> existingCountry = countryRepository.findByIsoCodeAndIsActiveTrue(createDto.isoCode());
        if (existingCountry.isPresent()) {
            throw new BusinessException(COUNTRY_ALREADY_EXISTS);
        }

        CountryEntity newEntity = countryMapper.toEntity(createDto);
        CountryEntity savedEntity = countryRepository.save(newEntity);

        return countryMapper.toDto(savedEntity);
    }

    @Override
    public void deleteCountry(Long id) {
        log.info("{} ID'li ülke pasife çekiliyor...", id);

        CountryEntity country = countryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(COUNTRY_NOT_FOUND));

        // SONARQUBE: NullPointerException riskine karşı güvenli boolean kontrolü
        if (Boolean.FALSE.equals(country.getIsActive())) {
            throw new BusinessException(PARAM_BAD_REQUEST);
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
                .orElseThrow(() -> new BusinessException(COUNTRY_NOT_FOUND));

        if (!country.getIsoCode().equalsIgnoreCase(updateDto.isoCode())) {
            Optional<CountryEntity> existingWithNewCode = countryRepository.findByIsoCodeAndIsActiveTrue(updateDto.isoCode());
            if (existingWithNewCode.isPresent()) {
                throw new BusinessException(COUNTRY_ALREADY_EXISTS);
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
                .orElseThrow(() -> new BusinessException(COUNTRY_NOT_FOUND));
        return countryMapper.toDto(country);
    }
}