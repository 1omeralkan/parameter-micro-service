package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.TownDto;
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
}