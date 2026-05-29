package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.service.RiskParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/risk-parameters")
@RequiredArgsConstructor
public class RiskParameterController {
    private final RiskParameterService service;

    @GetMapping("/{key}/value")
    public ResponseEntity<BigDecimal> getParamValue(@PathVariable String key) {
        return ResponseEntity.ok(service.getParamValue(key));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, BigDecimal>> getAllRiskParameters() {
        return ResponseEntity.ok(service.getAllActiveRiskParameters());
    }
}