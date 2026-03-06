package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CountryCreateDto;
import com.omeralkan.parameter.dto.CountryDto;
import com.omeralkan.parameter.dto.CountryUpdateDto;
import com.omeralkan.parameter.entity.CountryEntity;
import com.omeralkan.parameter.mapper.CountryMapper;
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
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    private CountryEntity mockCountry;
    private CountryDto mockCountryDto;

    @BeforeEach
    void setUp() {
        mockCountry = new CountryEntity();
        mockCountry.setId(1L);
        mockCountry.setIsoCode("TR");
        mockCountry.setName("Türkiye");
        mockCountry.setIsActive(true);

        mockCountryDto = new CountryDto(1L, "Türkiye", "TR", "+90");
    }

    // --- CREATE SENARYOLARI ---

    @Test
    @DisplayName("Başarılı Ülke Ekleme Senaryosu")
    void shouldCreateCountrySuccessfully() {
        CountryCreateDto createDto = new CountryCreateDto("Türkiye", "TR", "+90");

        given(countryRepository.findByIsoCodeAndIsActiveTrue("TR")).willReturn(Optional.empty());
        given(countryMapper.toEntity(createDto)).willReturn(mockCountry);
        given(countryRepository.save(mockCountry)).willReturn(mockCountry);
        given(countryMapper.toDto(mockCountry)).willReturn(mockCountryDto);

        CountryDto result = countryService.createCountry(createDto);

        assertNotNull(result);
        assertEquals("TR", result.isoCode());
        verify(countryRepository, times(1)).save(any(CountryEntity.class));
    }

    @Test
    @DisplayName("Aynı ISO Koduna Sahip Ülke Varsa Hata Fırlatmalı")
    void shouldThrowException_WhenIsoCodeAlreadyExists() {
        CountryCreateDto createDto = new CountryCreateDto("Türkiye", "TR", "+90");

        given(countryRepository.findByIsoCodeAndIsActiveTrue("TR")).willReturn(Optional.of(mockCountry));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> countryService.createCountry(createDto));
        assertTrue(ex.getMessage().contains("Bu ISO koduna sahip aktif bir ülke zaten var"));

        verify(countryRepository, never()).save(any()); // Veritabanına hiç gitmemeli!
    }

    // --- GET SENARYOLARI ---

    @Test
    @DisplayName("Aktif Tüm Ülkeleri Getirmeli")
    void shouldReturnAllActiveCountries() {
        given(countryRepository.findAllByIsActiveTrue()).willReturn(List.of(mockCountry));
        given(countryMapper.toDto(mockCountry)).willReturn(mockCountryDto);

        List<CountryDto> result = countryService.getAllActiveCountries();

        assertEquals(1, result.size());
        assertEquals("TR", result.get(0).isoCode());
    }

    @Test
    @DisplayName("ISO Kodu ile Aktif Ülke Bulunamadığında Hata Fırlatmalı")
    void shouldThrowException_WhenCountryNotFoundByIsoCode() {
        given(countryRepository.findByIsoCodeAndIsActiveTrue("US")).willReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> countryService.getCountryByIsoCode("US"));
        assertTrue(ex.getMessage().contains("Aktif ülke bulunamadı"));
    }

    // --- DELETE SENARYOLARI ---

    @Test
    @DisplayName("Ülke Başarıyla Pasife Çekilmeli (Soft Delete)")
    void shouldSoftDeleteCountrySuccessfully() {
        given(countryRepository.findById(1L)).willReturn(Optional.of(mockCountry));

        countryService.deleteCountry(1L);

        assertFalse(mockCountry.getIsActive()); // isActive false olmalı
        verify(countryRepository, times(1)).save(mockCountry);
    }

    @Test
    @DisplayName("Zaten Pasif Olan Ülke Silinmek İstenirse Hata Fırlatmalı")
    void shouldThrowException_WhenDeletingAlreadyPassiveCountry() {
        mockCountry.setIsActive(false); // Ülke zaten pasif
        given(countryRepository.findById(1L)).willReturn(Optional.of(mockCountry));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> countryService.deleteCountry(1L));
        assertTrue(ex.getMessage().contains("Bu ülke zaten pasif durumda"));
        verify(countryRepository, never()).save(any());
    }

    // --- UPDATE SENARYOLARI ---

    @Test
    @DisplayName("ISO Kodu Değişmeden Ülke Başarıyla Güncellenmeli")
    void shouldUpdateCountrySuccessfully_WhenIsoCodeNotChanged() {
        CountryUpdateDto updateDto = new CountryUpdateDto("Türkiye Cumhuriyeti", "TR", "+90");

        given(countryRepository.findById(1L)).willReturn(Optional.of(mockCountry));
        given(countryRepository.save(mockCountry)).willReturn(mockCountry);
        given(countryMapper.toDto(mockCountry)).willReturn(mockCountryDto);

        CountryDto result = countryService.updateCountry(1L, updateDto);

        assertNotNull(result);
        verify(countryMapper, times(1)).updateEntityFromDto(updateDto, mockCountry);
        verify(countryRepository, never()).findByIsoCodeAndIsActiveTrue(anyString()); // Kodu değişmediği için DB'ye tekrar sormamalı
    }
}