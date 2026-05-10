package com.omeralkan.parameter.dto;

public record PaymentTypeCreateDto(
        String code,
        String name,
        Integer minInstallment,
        Integer maxInstallment
) {}