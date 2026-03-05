package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.mapper.CityMapper;
import com.omeralkan.parameter.repository.CityRepository;
import com.omeralkan.parameter.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<CityDto> getCitiesByCountryId(Long countryId) {
        log.info("{} ID'li ülkeye ait aktif şehirler aranıyor...", countryId);

        // Repository üzerinden o ülkeye ait şehirleri çekiyoruz
        List<CityEntity> cities = cityRepository.findAllByCountryIdAndIsActiveTrue(countryId);

        return cities.stream()
                .map(cityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto getCityByPlateCode(String plateCode) {
        log.info("{} plakalı aktif şehir aranıyor...", plateCode);

        CityEntity city = cityRepository.findByPlateCodeAndIsActiveTrue(plateCode)
                .orElseThrow(() -> new RuntimeException("Aktif şehir bulunamadı! Plaka: " + plateCode));

        return cityMapper.toDto(city);
    }
}