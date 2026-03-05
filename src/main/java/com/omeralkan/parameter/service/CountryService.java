package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.CountryDto;
import java.util.List;

public interface CountryService {
    List<CountryDto> getAllActiveCountries();
    CountryDto getCountryByIsoCode(String isoCode);
}