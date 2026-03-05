package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.CityDto;
import java.util.List;

public interface CityService {
    // İş Kuralı 1: Belirli bir ülkenin aktif şehirlerini getir
    List<CityDto> getCitiesByCountryId(Long countryId);

    // İş Kuralı 2: Plakaya göre aktif şehri bul
    CityDto getCityByPlateCode(String plateCode);
}