package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;
import com.omeralkan.parameter.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    // 1. ID İLE GETİR
    // GET: http://localhost:8081/api/v1/countries/1
    @GetMapping("/{id:[0-9]+}") // Sadece rakam gelirse buraya girer
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    // 2. ISO KOD İLE GETİR
    // GET: http://localhost:8081/api/v1/countries/code/TR
    @GetMapping("/code/{isoCode}")
    public ResponseEntity<CountryDto> getCountryByIsoCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(countryService.getCountryByIsoCode(isoCode));
    }

    // POST: http://localhost:8081/api/v1/countries
    @PostMapping
    public ResponseEntity<CountryDto> createCountry(@Valid @RequestBody CountryCreateDto createDto) {
        CountryDto savedCountry = countryService.createCountry(createDto);
        return ResponseEntity.ok(savedCountry);
    }

    // DELETE: http://localhost:8081/api/v1/countries/3
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);

        return ResponseEntity.noContent().build();
    }

    // PUT: http://localhost:8081/api/v1/countries/1
    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryUpdateDto updateDto) {
        CountryDto updatedCountry = countryService.updateCountry(id, updateDto);
        return ResponseEntity.ok(updatedCountry);
    }
}