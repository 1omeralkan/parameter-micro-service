package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.CityCreateDto;
import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.dto.CityUpdateDto;

import java.util.List;

public interface CityService {
    // İş Kuralı 1: Belirli bir ülkenin aktif şehirlerini getir
    List<CityDto> getCitiesByCountryId(Long countryId);

    // İş Kuralı 2: Plakaya göre aktif şehri bul
    CityDto getCityByPlateCode(String plateCode);

    CityDto createCity(CityCreateDto createDto);

    void deleteCity(Long id);

    CityDto updateCity(Long id, CityUpdateDto updateDto);
}