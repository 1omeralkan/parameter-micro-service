package com.omeralkan.parameter.dto;

// Görüyorsun knk, class değil 'record' kullanıyoruz. Tertemiz!
public record CountryDto(
        Long id,
        String name,
        String isoCode,
        String phoneCode
) {
}