package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;

import java.util.List;

public interface TownService {
    // İş Kuralı: Sadece belirli bir şehre ait (Örn: 34 ID'li İstanbul) aktif ilçeleri getir
    List<TownDto> getTownsByCityId(Long cityId);

    TownDto createTown(TownCreateDto createDto);

    void deleteTown(Long id);

    TownDto updateTown(Long id, TownUpdateDto updateDto);
}