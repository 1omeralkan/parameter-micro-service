package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.repository.CoverageParameterRepository;
import com.omeralkan.parameter.service.CoverageParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoverageParameterServiceImpl implements CoverageParameterService {

    private final CoverageParameterRepository repository;

    @Override
    public BigDecimal getMultiplierByCode(String code) {
        return repository.findByCoverageCodeAndIsActiveTrue(code)
                .map(entity -> entity.getMultiplier())
                .orElseThrow(() -> new RuntimeException("Katsayı bulunamadı: " + code));
    }
}