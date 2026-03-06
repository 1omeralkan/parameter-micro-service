package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.TownEntity;
import org.springframework.stereotype.Component;

@Component
public class TownMapper {

    public TownDto toDto(TownEntity entity) {
        if (entity == null) {
            return null;
        }
        return new TownDto(
                entity.getId(),
                entity.getName(),
                entity.getCity() != null ? entity.getCity().getId() : null
        );
    }

    public TownEntity toEntity(TownCreateDto createDto, CityEntity city) {
        if (createDto == null) {
            return null;
        }
        TownEntity entity = new TownEntity();
        entity.setName(createDto.name());
        entity.setCity(city);
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