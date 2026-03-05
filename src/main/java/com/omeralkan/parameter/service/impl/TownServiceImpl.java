package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.entity.TownEntity;
import com.omeralkan.parameter.mapper.TownMapper;
import com.omeralkan.parameter.repository.TownRepository;
import com.omeralkan.parameter.service.TownService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j // Loglama işlemleri için
@Service
@RequiredArgsConstructor // Constructor Injection (Bağımlılıkları içeri alma)
public class TownServiceImpl implements TownService {

    // Bağımlılıkların tersine çevrilmesi (Dependency Inversion) prensibine uyuyoruz
    private final TownRepository townRepository;
    private final TownMapper townMapper;

    @Override
    public List<TownDto> getTownsByCityId(Long cityId) {
        log.info("{} ID'li şehre ait aktif ilçeler aranıyor...", cityId);

        // Sadece aktif ilçeleri getiren, Kaan abinin "tetikte olun" kuralına uyan metod
        List<TownEntity> towns = townRepository.findAllByCityIdAndIsActiveTrue(cityId);

        // Entity listesini DTO listesine dönüştürüyoruz (Java 8 Stream API gücü)
        return towns.stream()
                .map(townMapper::toDto)
                .collect(Collectors.toList());
    }
}