package com.omeralkan.parameter.dto;

public record TownDto(
        Long id,
        String name,
        Long cityId // Bağlı olduğu şehrin ID'si
) {
}