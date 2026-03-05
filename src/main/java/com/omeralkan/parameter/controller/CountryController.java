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

@RestController // Spring'e "Bu sınıf dış dünyaya JSON dönecek bir API'dir" der.
@RequestMapping("/api/v1/countries") // No Hardcoding: API'nin temel adresi ve versiyonu.
@RequiredArgsConstructor
public class CountryController {

    // Dependency Inversion: Controller, Service'in implementasyonunu bilmez, sadece interface'i tanır.
    private final CountryService countryService;

    // GET: http://localhost:8081/api/v1/countries
    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllActiveCountries() {
        List<CountryDto> countries = countryService.getAllActiveCountries();

        // ResponseEntity, HTTP statüsünü (200 OK) ve datayı (Body) kurumsal bir formatta sarmalar.
        return ResponseEntity.ok(countries);
    }

    // GET: http://localhost:8081/api/v1/countries/TR
    @GetMapping("/{isoCode}")
    public ResponseEntity<CountryDto> getCountryByIsoCode(@PathVariable String isoCode) {
        CountryDto country = countryService.getCountryByIsoCode(isoCode);
        return ResponseEntity.ok(country);
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

        // Kurumsal API kuralı: Silme işlemi başarılıysa ve geriye dönecek bir veri yoksa
        // HTTP 204 (No Content) dönülür.
        return ResponseEntity.noContent().build();
    }

    // PUT: http://localhost:8081/api/v1/countries/1
    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryUpdateDto updateDto) {
        CountryDto updatedCountry = countryService.updateCountry(id, updateDto);
        return ResponseEntity.ok(updatedCountry);
    }
}