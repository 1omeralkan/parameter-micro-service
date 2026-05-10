package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;
import com.omeralkan.parameter.service.TownService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/towns")
@RequiredArgsConstructor
public class TownController {

    private final TownService townService;

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<TownDto>> getTownsByCityId(@PathVariable Long cityId) {
        List<TownDto> towns = townService.getTownsByCityId(cityId);

        return ResponseEntity.ok(towns);
    }

    @PostMapping
    public ResponseEntity<TownDto> createTown(@Valid @RequestBody TownCreateDto createDto) {
        TownDto savedTown = townService.createTown(createDto);
        return ResponseEntity.ok(savedTown);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTown(@PathVariable Long id) {
        townService.deleteTown(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TownDto> updateTown(@PathVariable Long id, @Valid @RequestBody TownUpdateDto updateDto) {
        TownDto updatedTown = townService.updateTown(id, updateDto);
        return ResponseEntity.ok(updatedTown);
    }
}