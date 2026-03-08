package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.TownEntity;
import com.omeralkan.parameter.exception.BusinessException;
import com.omeralkan.parameter.mapper.TownMapper;
import com.omeralkan.parameter.repository.CityRepository;
import com.omeralkan.parameter.repository.TownRepository;
import com.omeralkan.parameter.service.TownService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final TownMapper townMapper;
    private final CityRepository cityRepository;

    // SONARQUBE: Tekrarlanan metinleri sabit değişkene aldık
    private static final String TOWN_NOT_FOUND = "TOWN-404";
    private static final String CITY_NOT_FOUND = "CITY-404";
    private static final String TOWN_ALREADY_EXISTS = "TOWN-409";
    private static final String PARAM_BAD_REQUEST = "PARAM-400";

    @Override
    public List<TownDto> getTownsByCityId(Long cityId) {
        log.info("{} ID'li şehre ait aktif ilçeler aranıyor...", cityId);

        List<TownEntity> towns = townRepository.findAllByCityIdAndIsActiveTrue(cityId);

        return towns.stream()
                .map(townMapper::toDto)
                .toList(); // SONARQUBE: Collectors yerine toList() kullanıldı
    }

    @Override
    public TownDto createTown(TownCreateDto createDto) {
        log.info("'{}' adında yeni ilçe ekleniyor...", createDto.name());

        CityEntity city = cityRepository.findById(createDto.cityId())
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(CITY_NOT_FOUND));

        Optional<TownEntity> existingTown = townRepository.findByNameAndCityIdAndIsActiveTrue(createDto.name(), createDto.cityId());
        if (existingTown.isPresent()) {
            throw new BusinessException(TOWN_ALREADY_EXISTS);
        }

        TownEntity newTown = townMapper.toEntity(createDto, city);
        TownEntity savedTown = townRepository.save(newTown);

        return townMapper.toDto(savedTown);
    }

    @Override
    public void deleteTown(Long id) {
        log.info("{} ID'li ilçe pasife çekiliyor...", id);

        TownEntity town = townRepository.findById(id)
                .orElseThrow(() -> new BusinessException(TOWN_NOT_FOUND));

        // SONARQUBE: NullPointerException riskine karşı güvenli boolean kontrolü
        if (Boolean.FALSE.equals(town.getIsActive())) {
            throw new BusinessException(PARAM_BAD_REQUEST);
        }

        town.setIsActive(false);
        townRepository.save(town);

        log.info("{} ID'li ilçe başarıyla pasife çekildi.", id);
    }

    @Override
    public TownDto updateTown(Long id, TownUpdateDto updateDto) {
        log.info("{} ID'li ilçe güncelleniyor...", id);

        TownEntity town = townRepository.findById(id)
                .filter(TownEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(TOWN_NOT_FOUND));

        CityEntity city = cityRepository.findById(updateDto.cityId())
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(CITY_NOT_FOUND));

        if (!town.getName().equalsIgnoreCase(updateDto.name()) || !town.getCity().getId().equals(updateDto.cityId())) {
            Optional<TownEntity> existing = townRepository.findByNameAndCityIdAndIsActiveTrue(updateDto.name(), updateDto.cityId());
            if (existing.isPresent()) {
                throw new BusinessException(TOWN_ALREADY_EXISTS);
            }
        }

        townMapper.updateEntityFromDto(updateDto, town, city);
        TownEntity updatedTown = townRepository.save(town);

        return townMapper.toDto(updatedTown);
    }
}