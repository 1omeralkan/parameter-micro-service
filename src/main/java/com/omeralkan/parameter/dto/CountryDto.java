package com.omeralkan.parameter.dto;

public record CountryDto(
        Long id,
        String name,
        String isoCode,
        String phoneCode
) {
}