package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.service.CoverageParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/coverage-parameters")
@RequiredArgsConstructor
public class CoverageParameterController {

    private final CoverageParameterService service;

    @GetMapping("/{code}/multiplier")
    public ResponseEntity<BigDecimal> getMultiplierByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getMultiplierByCode(code));
    }
}