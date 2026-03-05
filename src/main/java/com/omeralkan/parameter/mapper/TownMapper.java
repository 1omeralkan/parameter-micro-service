package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.TownEntity;
import org.springframework.stereotype.Component;

@Component // Spring'e bu sınıfı hafızaya almasını (Bean yapmasını) söylüyoruz
public class TownMapper {

    // Veritabanı nesnesini (Entity), dışarıya açılacak güvenli kutuya (DTO) çevirir
    public TownDto toDto(TownEntity entity) {
        if (entity == null) {
            return null;
        }
        return new TownDto(
                entity.getId(),
                entity.getName(),
                // İlişkisel tablo: İlçenin bağlı olduğu Şehrin sadece ID'sini alıyoruz
                entity.getCity() != null ? entity.getCity().getId() : null
        );
    }

    // Dışarıdan gelen DTO ve veritabanından bulduğumuz Şehir nesnesini birleştirip İlçe Entity'si yapar
    public TownEntity toEntity(TownCreateDto createDto, CityEntity city) {
        if (createDto == null) {
            return null;
        }
        TownEntity entity = new TownEntity();
        entity.setName(createDto.name());
        entity.setCity(city); // Hiyerarşik bağ burada kuruluyor!
        return entity;
    }

    public void updateEntityFromDto(TownUpdateDto dto, TownEntity entity, CityEntity city) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.name());
        entity.setCity(city);
    }
}