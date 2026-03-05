package com.omeralkan.parameter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CityUpdateDto(
        @NotBlank(message = "Şehir adı boş olamaz!")
        @Size(max = 100, message = "Şehir adı en fazla 100 karakter olabilir")
        String name,

        @NotBlank(message = "Plaka kodu boş olamaz!")
        @Size(max = 5, message = "Plaka kodu en fazla 5 karakter olabilir")
        String plateCode,

        @NotNull(message = "Şehrin bağlı olduğu ülke ID'si boş olamaz!")
        Long countryId
) {
}