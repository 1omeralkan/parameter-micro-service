package com.omeralkan.parameter.service;

import java.math.BigDecimal;

public interface CoverageParameterService {
    BigDecimal getMultiplierByCode(String code);
}