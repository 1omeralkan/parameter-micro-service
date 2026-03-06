package com.omeralkan.parameter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CountryCreateDto(

        @NotBlank(message = "Ülke adı boş olamaz!")
        @Size(max = 100, message = "Ülke adı en fazla 100 karakter olabilir")
        String name,

        @NotBlank(message = "ISO kodu boş olamaz!")
        @Size(min = 2, max = 2, message = "ISO kodu tam olarak 2 karakter olmalıdır (Örn: TR)")
        String isoCode,

        @NotBlank(message = "Telefon kodu boş olamaz!")
        @Size(max = 10, message = "Telefon kodu çok uzun olamaz (Örn: +90)")
        String phoneCode
) {
}