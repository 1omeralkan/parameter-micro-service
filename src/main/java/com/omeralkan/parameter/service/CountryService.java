package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;

import java.util.List;

public interface CountryService {
    List<CountryDto> getAllActiveCountries();
    CountryDto getCountryByIsoCode(String isoCode);
    CountryDto getCountryById(Long id);
    CountryDto createCountry(CountryCreateDto createDto);
    void deleteCountry(Long id);
    CountryDto updateCountry(Long id, CountryUpdateDto updateDto);
}