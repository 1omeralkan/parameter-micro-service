package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.TownCreateDto;
import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.dto.TownUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.TownEntity;
import com.omeralkan.parameter.mapper.TownMapper;
import com.omeralkan.parameter.repository.CityRepository;
import com.omeralkan.parameter.repository.TownRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TownServiceImplTest {

    @Mock
    private TownRepository townRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private TownMapper townMapper;

    @InjectMocks
    private TownServiceImpl townService;

    private CityEntity mockCity;
    private TownEntity mockTown;
    private TownDto mockTownDto;

    @BeforeEach
    void setUp() {
        mockCity = new CityEntity();
        mockCity.setId(10L);
        mockCity.setName("İstanbul");
        mockCity.setIsActive(true);

        mockTown = new TownEntity();
        mockTown.setId(100L);
        mockTown.setName("Kadıköy");
        mockTown.setCity(mockCity);
        mockTown.setIsActive(true);

        mockTownDto = new TownDto(100L, "Kadıköy", 10L);
    }

    // --- CREATE SENARYOLARI ---

    @Test
    @DisplayName("Başarılı İlçe Ekleme Senaryosu")
    void shouldCreateTownSuccessfully() {
        TownCreateDto createDto = new TownCreateDto("Kadıköy", 10L);

        // 1. Şehir var mı diye bakacak
        given(cityRepository.findById(10L)).willReturn(Optional.of(mockCity));
        // 2. Bu isimde bir ilçe bu şehirde var mı diye bakacak
        given(townRepository.findByNameAndCityIdAndIsActiveTrue("Kadıköy", 10L)).willReturn(Optional.empty());

        given(townMapper.toEntity(createDto, mockCity)).willReturn(mockTown);
        given(townRepository.save(mockTown)).willReturn(mockTown);
        given(townMapper.toDto(mockTown)).willReturn(mockTownDto);

        TownDto result = townService.createTown(createDto);

        assertNotNull(result);
        assertEquals("Kadıköy", result.name());
        verify(townRepository, times(1)).save(any(TownEntity.class));
    }

    @Test
    @DisplayName("Bağlanmaya Çalışılan Şehir Yoksa veya Pasifse Hata Fırlatmalı")
    void shouldThrowException_WhenCityNotFoundOrPassiveOnCreate() {
        TownCreateDto createDto = new TownCreateDto("Kadıköy", 99L); // 99 ID'li şehir yok

        given(cityRepository.findById(99L)).willReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> townService.createTown(createDto));
        assertTrue(ex.getMessage().contains("Bağlanmaya çalışılan şehir bulunamadı veya pasif"));
        verify(townRepository, never()).save(any());
    }

    @Test
    @DisplayName("Aynı Şehirde Aynı İsimde İlçe Varsa Hata Fırlatmalı")
    void shouldThrowException_WhenTownNameAlreadyExistsInCityOnCreate() {
        TownCreateDto createDto = new TownCreateDto("Kadıköy", 10L);

        given(cityRepository.findById(10L)).willReturn(Optional.of(mockCity));
        given(townRepository.findByNameAndCityIdAndIsActiveTrue("Kadıköy", 10L)).willReturn(Optional.of(mockTown));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> townService.createTown(createDto));
        assertTrue(ex.getMessage().contains("adında aktif bir ilçe zaten var"));
        verify(townRepository, never()).save(any());
    }

    // --- GET SENARYOLARI ---

    @Test
    @DisplayName("Şehir ID'sine Göre Aktif İlçeleri Getirmeli")
    void shouldReturnTownsByCityId() {
        given(townRepository.findAllByCityIdAndIsActiveTrue(10L)).willReturn(List.of(mockTown));
        given(townMapper.toDto(mockTown)).willReturn(mockTownDto);

        List<TownDto> result = townService.getTownsByCityId(10L);

        assertEquals(1, result.size());
        assertEquals("Kadıköy", result.get(0).name());
    }

    // --- DELETE SENARYOLARI ---

    @Test
    @DisplayName("İlçe Başarıyla Pasife Çekilmeli (Soft Delete)")
    void shouldSoftDeleteTownSuccessfully() {
        given(townRepository.findById(100L)).willReturn(Optional.of(mockTown));

        townService.deleteTown(100L);

        assertFalse(mockTown.getIsActive());
        verify(townRepository, times(1)).save(mockTown);
    }

    // --- UPDATE SENARYOLARI ---

    @Test
    @DisplayName("İlçe Başarıyla Güncellenmeli")
    void shouldUpdateTownSuccessfully() {
        TownUpdateDto updateDto = new TownUpdateDto("Yeni Kadıköy", 10L);

        given(townRepository.findById(100L)).willReturn(Optional.of(mockTown));
        given(cityRepository.findById(10L)).willReturn(Optional.of(mockCity));
        given(townRepository.save(mockTown)).willReturn(mockTown);
        given(townMapper.toDto(mockTown)).willReturn(mockTownDto);

        TownDto result = townService.updateTown(100L, updateDto);

        assertNotNull(result);
        verify(townMapper, times(1)).updateEntityFromDto(updateDto, mockTown, mockCity);
    }
}