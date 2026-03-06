package com.omeralkan.parameter.dto;

public record CityDto(
        Long id,
        String name,
        String plateCode,
        Long countryId
) {
}