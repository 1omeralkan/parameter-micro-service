package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.CityCreateDto;
import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.dto.CityUpdateDto;

import java.util.List;

public interface CityService {
    List<CityDto> getCitiesByCountryId(Long countryId);
    CityDto getCityByPlateCode(String plateCode);
    CityDto getCityById(Long id);
    CityDto createCity(CityCreateDto createDto);
    void deleteCity(Long id);
    CityDto updateCity(Long id, CityUpdateDto updateDto);
}