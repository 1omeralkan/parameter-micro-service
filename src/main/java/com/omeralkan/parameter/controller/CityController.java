package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Spring'e bu sınıfın JSON dönen bir REST API olduğunu söylüyoruz
@RequestMapping("/api/v1/cities") // Endpoint'imizin ana giriş kapısı
@RequiredArgsConstructor
public class CityController {

    // Dependency Inversion: Arayüze bağımlıyız, implementasyona değil! [cite: 2026-02-23]
    private final CityService cityService;

    // GET: http://localhost:8081/api/v1/cities/country/1
    // Amacı: Belirli bir ülkenin (Örn: ID'si 1 olan Türkiye'nin) şehirlerini getirmek
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<CityDto>> getCitiesByCountryId(@PathVariable Long countryId) {
        List<CityDto> cities = cityService.getCitiesByCountryId(countryId);
        return ResponseEntity.ok(cities);
    }

    // GET: http://localhost:8081/api/v1/cities/plate/34
    // Amacı: Plaka koduna göre nokta atışı şehir bulmak
    @GetMapping("/plate/{plateCode}")
    public ResponseEntity<CityDto> getCityByPlateCode(@PathVariable String plateCode) {
        CityDto city = cityService.getCityByPlateCode(plateCode);
        return ResponseEntity.ok(city);
    }
}