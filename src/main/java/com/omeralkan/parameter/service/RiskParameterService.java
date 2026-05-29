package com.omeralkan.parameter.service;
import java.math.BigDecimal;
import java.util.Map;

public interface RiskParameterService {
    BigDecimal getParamValue(String paramKey);
    Map<String, BigDecimal> getAllActiveRiskParameters();
}