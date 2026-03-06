package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CityCreateDto;
import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.dto.CityUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.CountryEntity;
import com.omeralkan.parameter.mapper.CityMapper;
import com.omeralkan.parameter.repository.CityRepository;
import com.omeralkan.parameter.repository.CountryRepository;
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
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityServiceImpl cityService;

    private CountryEntity mockCountry;
    private CityEntity mockCity;
    private CityDto mockCityDto;

    @BeforeEach
    void setUp() {
        mockCountry = new CountryEntity();
        mockCountry.setId(1L);
        mockCountry.setName("Türkiye");
        mockCountry.setIsActive(true);

        mockCity = new CityEntity();
        mockCity.setId(10L);
        mockCity.setPlateCode("34");
        mockCity.setName("İstanbul");
        mockCity.setCountry(mockCountry);
        mockCity.setIsActive(true);

        mockCityDto = new CityDto(10L, "İstanbul", "34", 1L);
    }

    // --- CREATE SENARYOLARI ---

    @Test
    @DisplayName("Başarılı Şehir Ekleme Senaryosu")
    void shouldCreateCitySuccessfully() {
        CityCreateDto createDto = new CityCreateDto("İstanbul", "34", 1L);

        // 1. Ülke var mı ve aktif mi diye bakacak
        given(countryRepository.findById(1L)).willReturn(Optional.of(mockCountry));
        // 2. Bu plaka var mı diye bakacak
        given(cityRepository.findByPlateCodeAndIsActiveTrue("34")).willReturn(Optional.empty());

        given(cityMapper.toEntity(createDto, mockCountry)).willReturn(mockCity);
        given(cityRepository.save(mockCity)).willReturn(mockCity);
        given(cityMapper.toDto(mockCity)).willReturn(mockCityDto);

        CityDto result = cityService.createCity(createDto);

        assertNotNull(result);
        assertEquals("34", result.plateCode());
        verify(cityRepository, times(1)).save(any(CityEntity.class));
    }

    @Test
    @DisplayName("Bağlanmaya Çalışılan Ülke Yoksa veya Pasifse Hata Fırlatmalı")
    void shouldThrowException_WhenCountryNotFoundOrPassiveOnCreate() {
        CityCreateDto createDto = new CityCreateDto("İstanbul", "34", 99L); // 99 ID'li ülke yok

        given(countryRepository.findById(99L)).willReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cityService.createCity(createDto));
        assertTrue(ex.getMessage().contains("Bağlanmaya çalışılan ülke bulunamadı veya pasif"));
        verify(cityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Aynı Plaka Koduna Sahip Şehir Varsa Hata Fırlatmalı")
    void shouldThrowException_WhenPlateCodeAlreadyExistsOnCreate() {
        CityCreateDto createDto = new CityCreateDto("İstanbul", "34", 1L);

        given(countryRepository.findById(1L)).willReturn(Optional.of(mockCountry));
        given(cityRepository.findByPlateCodeAndIsActiveTrue("34")).willReturn(Optional.of(mockCity));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cityService.createCity(createDto));
        assertTrue(ex.getMessage().contains("Bu plaka koduna sahip aktif bir şehir zaten var"));
        verify(cityRepository, never()).save(any());
    }

    // --- GET SENARYOLARI ---

    @Test
    @DisplayName("Ülke ID'sine Göre Aktif Şehirleri Getirmeli")
    void shouldReturnCitiesByCountryId() {
        given(cityRepository.findAllByCountryIdAndIsActiveTrue(1L)).willReturn(List.of(mockCity));
        given(cityMapper.toDto(mockCity)).willReturn(mockCityDto);

        List<CityDto> result = cityService.getCitiesByCountryId(1L);

        assertEquals(1, result.size());
        assertEquals("34", result.get(0).plateCode());
    }

    // --- DELETE SENARYOLARI ---

    @Test
    @DisplayName("Şehir Başarıyla Pasife Çekilmeli (Soft Delete)")
    void shouldSoftDeleteCitySuccessfully() {
        given(cityRepository.findById(10L)).willReturn(Optional.of(mockCity));

        cityService.deleteCity(10L);

        assertFalse(mockCity.getIsActive());
        verify(cityRepository, times(1)).save(mockCity);
    }

    // --- UPDATE SENARYOLARI ---

    @Test
    @DisplayName("Şehir Başarıyla Güncellenmeli")
    void shouldUpdateCitySuccessfully() {
        CityUpdateDto updateDto = new CityUpdateDto("Yeni İstanbul", "34", 1L);

        given(cityRepository.findById(10L)).willReturn(Optional.of(mockCity));
        given(countryRepository.findById(1L)).willReturn(Optional.of(mockCountry));
        given(cityRepository.save(mockCity)).willReturn(mockCity);
        given(cityMapper.toDto(mockCity)).willReturn(mockCityDto);

        CityDto result = cityService.updateCity(10L, updateDto);

        assertNotNull(result);
        verify(cityMapper, times(1)).updateEntityFromDto(updateDto, mockCity, mockCountry);
    }
}