package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;

import java.util.List;

public interface TownService {
    List<TownDto> getTownsByCityId(Long cityId);

    TownDto createTown(TownCreateDto createDto);

    void deleteTown(Long id);

    TownDto updateTown(Long id, TownUpdateDto updateDto);
}