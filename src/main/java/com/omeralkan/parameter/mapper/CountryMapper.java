package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;
import com.omeralkan.parameter.entity.CountryEntity;
import org.springframework.stereotype.Component;

@Component // Spring'e "Bu sınıfı al, bir fasulye (Bean) yap ve hafızanda tut" diyoruz.
public class CountryMapper {

    // Veritabanından gelen nesneyi (Entity), dışarı çıkacak kutuya (DTO) çevirir
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

    // İleride dışarıdan gelen DTO'yu, veritabanına kaydedilecek Entity'ye çevirmek için
    public CountryEntity toEntity(CountryDto dto) {
        if (dto == null) {
            return null;
        }
        CountryEntity entity = new CountryEntity();
        // ID set etmiyoruz çünkü yeni kayıtta ID'yi veritabanı (Auto Increment) verir
        entity.setName(dto.name());
        entity.setIsoCode(dto.isoCode());
        entity.setPhoneCode(dto.phoneCode());
        return entity;
    }

    // Dışarıdan gelen DTO'yu, veritabanına kaydedilecek Entity'ye çevirir
    public CountryEntity toEntity(CountryCreateDto createDto) {
        if (createDto == null) {
            return null;
        }
        CountryEntity entity = new CountryEntity();
        // ID set etmiyoruz, veritabanı verecek.
        // version, is_active, created_at gibi alanları BaseEntity otomatik halledecek.
        entity.setName(createDto.name());
        entity.setIsoCode(createDto.isoCode());
        entity.setPhoneCode(createDto.phoneCode());
        return entity;
    }

    // Dışarıdan gelen DTO'daki yeni verileri, veritabanından çektiğimiz MEVCUT Entity'nin üzerine yazar
    public void updateEntityFromDto(CountryUpdateDto dto, CountryEntity entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.name());
        entity.setIsoCode(dto.isoCode());
        entity.setPhoneCode(dto.phoneCode());
    }
}