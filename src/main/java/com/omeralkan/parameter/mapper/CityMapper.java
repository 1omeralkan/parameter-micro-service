package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.CityCreateDto;
import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.dto.CityUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.CountryEntity;
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
    // Dışarıdan gelen DTO ve veritabanından bulduğumuz Ülke nesnesini birleştirip Şehir Entity'si yapar
    public CityEntity toEntity(CityCreateDto createDto, CountryEntity country) {
        if (createDto == null) {
            return null;
        }
        CityEntity entity = new CityEntity();
        entity.setName(createDto.name());
        entity.setPlateCode(createDto.plateCode());
        entity.setCountry(country); // İşte hiyerarşik bağ burada kuruluyor!
        return entity;
    }

    public void updateEntityFromDto(CityUpdateDto dto, CityEntity entity, CountryEntity country) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.name());
        entity.setPlateCode(dto.plateCode());
        entity.setCountry(country);
    }
}