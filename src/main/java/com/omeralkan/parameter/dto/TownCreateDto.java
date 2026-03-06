package com.omeralkan.parameter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TownCreateDto(

        @NotBlank(message = "İlçe adı boş olamaz!")
        @Size(max = 100, message = "İlçe adı en fazla 100 karakter olabilir")
        String name,

        @NotNull(message = "İlçenin bağlı olduğu şehir ID'si boş olamaz!")
        Long cityId
) {
}