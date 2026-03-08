package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CityCreateDto;
import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.dto.CityUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.CountryEntity;
import com.omeralkan.parameter.exception.BusinessException;
import com.omeralkan.parameter.mapper.CityMapper;
import com.omeralkan.parameter.repository.CityRepository;
import com.omeralkan.parameter.repository.CountryRepository;
import com.omeralkan.parameter.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CountryRepository countryRepository;

    // SONARQUBE: Tekrarlanan metinleri sabit değişkene aldık
    private static final String CITY_NOT_FOUND = "CITY-404";
    private static final String COUNTRY_NOT_FOUND = "COUNTRY-404";
    private static final String CITY_ALREADY_EXISTS = "CITY-409";
    private static final String PARAM_BAD_REQUEST = "PARAM-400";

    @Override
    public List<CityDto> getCitiesByCountryId(Long countryId) {
        log.info("{} ID'li ülkeye ait aktif şehirler aranıyor...", countryId);

        List<CityEntity> cities = cityRepository.findAllByCountryIdAndIsActiveTrue(countryId);

        return cities.stream()
                .map(cityMapper::toDto)
                .toList(); // SONARQUBE: Collectors yerine toList() kullanıldı
    }

    @Override
    public CityDto getCityByPlateCode(String plateCode) {
        log.info("{} plakalı aktif şehir aranıyor...", plateCode);

        CityEntity city = cityRepository.findByPlateCodeAndIsActiveTrue(plateCode)
                .orElseThrow(() -> new BusinessException(CITY_NOT_FOUND));

        return cityMapper.toDto(city);
    }

    @Override
    public CityDto createCity(CityCreateDto createDto) {
        log.info("{} plakalı yeni şehir ekleniyor...", createDto.plateCode());

        CountryEntity country = countryRepository.findById(createDto.countryId())
                .filter(CountryEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(COUNTRY_NOT_FOUND));

        Optional<CityEntity> existingCity = cityRepository.findByPlateCodeAndIsActiveTrue(createDto.plateCode());
        if (existingCity.isPresent()) {
            throw new BusinessException(CITY_ALREADY_EXISTS);
        }

        CityEntity newCity = cityMapper.toEntity(createDto, country);
        CityEntity savedCity = cityRepository.save(newCity);

        return cityMapper.toDto(savedCity);
    }

    @Override
    public void deleteCity(Long id) {
        log.info("{} ID'li şehir pasife çekiliyor...", id);

        CityEntity city = cityRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CITY_NOT_FOUND));

        // SONARQUBE: NullPointerException riskine karşı güvenli boolean kontrolü
        if (Boolean.FALSE.equals(city.getIsActive())) {
            throw new BusinessException(PARAM_BAD_REQUEST);
        }

        city.setIsActive(false);
        cityRepository.save(city);

        log.info("{} ID'li şehir başarıyla pasife çekildi.", id);
    }

    @Override
    public CityDto updateCity(Long id, CityUpdateDto updateDto) {
        log.info("{} ID'li şehir güncelleniyor...", id);

        CityEntity city = cityRepository.findById(id)
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(CITY_NOT_FOUND));

        CountryEntity country = countryRepository.findById(updateDto.countryId())
                .filter(CountryEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(COUNTRY_NOT_FOUND));

        if (!city.getPlateCode().equalsIgnoreCase(updateDto.plateCode())) {
            Optional<CityEntity> existing = cityRepository.findByPlateCodeAndIsActiveTrue(updateDto.plateCode());
            if (existing.isPresent()) {
                throw new BusinessException(CITY_ALREADY_EXISTS);
            }
        }

        cityMapper.updateEntityFromDto(updateDto, city, country);
        CityEntity updatedCity = cityRepository.save(city);

        return cityMapper.toDto(updatedCity);
    }

    @Override
    public CityDto getCityById(Long id) {
        log.info("{} ID'li aktif şehir aranıyor...", id);
        CityEntity city = cityRepository.findById(id)
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(CITY_NOT_FOUND));
        return cityMapper.toDto(city);
    }
}