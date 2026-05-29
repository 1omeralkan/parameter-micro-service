package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;
import com.omeralkan.parameter.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllActiveCountries());
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    @GetMapping("/code/{isoCode}")
    public ResponseEntity<CountryDto> getCountryByIsoCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(countryService.getCountryByIsoCode(isoCode));
    }

    @PostMapping
    public ResponseEntity<CountryDto> createCountry(@Valid @RequestBody CountryCreateDto createDto) {
        CountryDto savedCountry = countryService.createCountry(createDto);
        return ResponseEntity.ok(savedCountry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryUpdateDto updateDto) {
        CountryDto updatedCountry = countryService.updateCountry(id, updateDto);
        return ResponseEntity.ok(updatedCountry);
    }
}