package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.entity.CityEntity;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public CityDto toDto(CityEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CityDto(
                entity.getId(),
                entity.getName(),
                entity.getPlateCode(),
                // İlişkisel tablo: Şehrin içindeki Ülke nesnesine gidip onun ID'sini alıyoruz
                entity.getCountry() != null ? entity.getCountry().getId() : null
        );
    }
}