package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;
import com.omeralkan.parameter.entity.CountryEntity;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    public CountryDto toDto(CountryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CountryDto(
                entity.getId(),
                entity.getName(),
                entity.getIsoCode(),
                entity.getPhoneCode()
        );
    }

    public CountryEntity toEntity(CountryDto dto) {
        if (dto == null) {
            return null;
        }
        CountryEntity entity = new CountryEntity();
        entity.setName(dto.name());
        entity.setIsoCode(dto.isoCode());
        entity.setPhoneCode(dto.phoneCode());
        return entity;
    }

    public CountryEntity toEntity(CountryCreateDto createDto) {
        if (createDto == null) {
            return null;
        }
        CountryEntity entity = new CountryEntity();
        entity.setName(createDto.name());
        entity.setIsoCode(createDto.isoCode());
        entity.setPhoneCode(createDto.phoneCode());
        return entity;
    }

    public void updateEntityFromDto(CountryUpdateDto dto, CountryEntity entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.name());
        entity.setIsoCode(dto.isoCode());
        entity.setPhoneCode(dto.phoneCode());
    }
}