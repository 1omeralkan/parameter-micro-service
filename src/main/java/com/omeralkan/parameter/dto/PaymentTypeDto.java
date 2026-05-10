package com.omeralkan.parameter.dto;

public record PaymentTypeDto(
        Long id,
        String code,
        String name,
        Integer minInstallment,
        Integer maxInstallment,
        Boolean isActive
) {}