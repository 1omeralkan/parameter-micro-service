package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.dto.CityCreateDto;
import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.dto.CityUpdateDto;
import com.omeralkan.parameter.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    // GET: http://localhost:8081/api/v1/cities/1
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    // ÜLKE ID'SİNE GÖRE LİSTELE
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<CityDto>> getCitiesByCountryId(@PathVariable Long countryId) {
        return ResponseEntity.ok(cityService.getCitiesByCountryId(countryId));
    }

    // PLAKA İLE GETİR
    @GetMapping("/plate/{plateCode}")
    public ResponseEntity<CityDto> getCityByPlateCode(@PathVariable String plateCode) {
        return ResponseEntity.ok(cityService.getCityByPlateCode(plateCode));
    }

    @PostMapping
    public ResponseEntity<CityDto> createCity(@Valid @RequestBody CityCreateDto createDto) {
        CityDto savedCity = cityService.createCity(createDto);
        return ResponseEntity.ok(savedCity);
    }

    // DELETE: http://localhost:8081/api/v1/cities/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable Long id, @Valid @RequestBody CityUpdateDto updateDto) {
        CityDto updatedCity = cityService.updateCity(id, updateDto);
        return ResponseEntity.ok(updatedCity);
    }
}