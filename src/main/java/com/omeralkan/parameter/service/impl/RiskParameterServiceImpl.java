package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.entity.RiskParameterEntity;
import com.omeralkan.parameter.repository.RiskParameterRepository;
import com.omeralkan.parameter.service.RiskParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RiskParameterServiceImpl implements RiskParameterService {
    private final RiskParameterRepository repository;

    @Override
    public BigDecimal getParamValue(String paramKey) {
        return repository.findByParamKeyAndIsActiveTrue(paramKey)
                .map(entity -> entity.getParamValue())
                .orElseThrow(() -> new RuntimeException("Risk parametresi bulunamadı: " + paramKey));
    }

    @Override
    public Map<String, BigDecimal> getAllActiveRiskParameters() {
        return repository.findAllByIsActiveTrue().stream()
                .collect(java.util.stream.Collectors.toMap(
                        RiskParameterEntity::getParamKey,
                        RiskParameterEntity::getParamValue
                ));
    }
}