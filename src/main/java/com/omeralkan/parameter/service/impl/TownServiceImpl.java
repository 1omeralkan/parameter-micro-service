package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.TownEntity;
import com.omeralkan.parameter.mapper.TownMapper;
import com.omeralkan.parameter.repository.CityRepository;
import com.omeralkan.parameter.repository.TownRepository;
import com.omeralkan.parameter.service.TownService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final TownMapper townMapper;
    private final CityRepository cityRepository;

    @Override
    public List<TownDto> getTownsByCityId(Long cityId) {
        log.info("{} ID'li şehre ait aktif ilçeler aranıyor...", cityId);

        List<TownEntity> towns = townRepository.findAllByCityIdAndIsActiveTrue(cityId);

        return towns.stream()
                .map(townMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TownDto createTown(TownCreateDto createDto) {
        log.info("'{}' adında yeni ilçe ekleniyor...", createDto.name());

        // Gelen cityId veritabanında gerçekten var mı ve aktif mi?
        CityEntity city = cityRepository.findById(createDto.cityId())
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Bağlanmaya çalışılan şehir bulunamadı veya pasif! ID: " + createDto.cityId()));

        Optional<TownEntity> existingTown = townRepository.findByNameAndCityIdAndIsActiveTrue(createDto.name(), createDto.cityId());
        if (existingTown.isPresent()) {
            throw new RuntimeException("Bu şehirde '" + createDto.name() + "' adında aktif bir ilçe zaten var!");
        }
        TownEntity newTown = townMapper.toEntity(createDto, city);
        TownEntity savedTown = townRepository.save(newTown);

        return townMapper.toDto(savedTown);
    }

    @Override
    public void deleteTown(Long id) {
        log.info("{} ID'li ilçe pasife çekiliyor...", id);

        TownEntity town = townRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinmek istenen ilçe bulunamadı! ID: " + id));

        if (!town.getIsActive()) {
            throw new RuntimeException("Bu ilçe zaten pasif durumda! ID: " + id);
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
                .orElseThrow(() -> new RuntimeException("Güncellenecek aktif ilçe bulunamadı! ID: " + id));

        CityEntity city = cityRepository.findById(updateDto.cityId())
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Bağlanmaya çalışılan şehir bulunamadı veya pasif! ID: " + updateDto.cityId()));

        if (!town.getName().equalsIgnoreCase(updateDto.name()) || !town.getCity().getId().equals(updateDto.cityId())) {
            Optional<TownEntity> existing = townRepository.findByNameAndCityIdAndIsActiveTrue(updateDto.name(), updateDto.cityId());
            if (existing.isPresent()) {
                throw new RuntimeException("Bu şehirde '" + updateDto.name() + "' adında aktif bir ilçe zaten var!");
            }
        }

        townMapper.updateEntityFromDto(updateDto, town, city);
        TownEntity updatedTown = townRepository.save(town);

        return townMapper.toDto(updatedTown);
    }
}