package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.CityCreateDto;
import com.omeralkan.parameter.dto.CityDto;
import com.omeralkan.parameter.dto.CityUpdateDto;
import com.omeralkan.parameter.entity.CityEntity;
import com.omeralkan.parameter.entity.CountryEntity;
import com.omeralkan.parameter.mapper.CityMapper;
import com.omeralkan.parameter.repository.CityRepository;
import com.omeralkan.parameter.repository.CountryRepository;
import com.omeralkan.parameter.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CountryRepository countryRepository;

    @Override
    public List<CityDto> getCitiesByCountryId(Long countryId) {
        log.info("{} ID'li ülkeye ait aktif şehirler aranıyor...", countryId);

        // Repository üzerinden o ülkeye ait şehirleri çekiyoruz
        List<CityEntity> cities = cityRepository.findAllByCountryIdAndIsActiveTrue(countryId);

        return cities.stream()
                .map(cityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto getCityByPlateCode(String plateCode) {
        log.info("{} plakalı aktif şehir aranıyor...", plateCode);

        CityEntity city = cityRepository.findByPlateCodeAndIsActiveTrue(plateCode)
                .orElseThrow(() -> new RuntimeException("Aktif şehir bulunamadı! Plaka: " + plateCode));

        return cityMapper.toDto(city);
    }

    @Override
    public CityDto createCity(CityCreateDto createDto) {
        log.info("{} plakalı yeni şehir ekleniyor...", createDto.plateCode());

        //Gelen countryId veritabanında gerçekten var mı ve aktif mi?
        CountryEntity country = countryRepository.findById(createDto.countryId())
                .filter(CountryEntity::getIsActive) // Sadece is_active = true olanı kabul et
                .orElseThrow(() -> new RuntimeException("Bağlanmaya çalışılan ülke bulunamadı veya pasif! ID: " + createDto.countryId()));

        //Bu plaka koduna sahip aktif bir şehir zaten var mı?
        Optional<CityEntity> existingCity = cityRepository.findByPlateCodeAndIsActiveTrue(createDto.plateCode());
        if (existingCity.isPresent()) {
            throw new RuntimeException("Bu plaka koduna sahip aktif bir şehir zaten var: " + createDto.plateCode());
        }

        CityEntity newCity = cityMapper.toEntity(createDto, country);
        CityEntity savedCity = cityRepository.save(newCity);

        return cityMapper.toDto(savedCity);
    }

    @Override
    public void deleteCity(Long id) {
        log.info("{} ID'li şehir pasife çekiliyor...", id);

        CityEntity city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinmek istenen şehir bulunamadı! ID: " + id));

        if (!city.getIsActive()) {
            throw new RuntimeException("Bu şehir zaten pasif durumda! ID: " + id);
        }

        city.setIsActive(false);
        cityRepository.save(city);

        log.info("{} ID'li şehir başarıyla pasife çekildi.", id);
    }

    @Override
    public CityDto updateCity(Long id, CityUpdateDto updateDto) {
        log.info("{} ID'li şehir güncelleniyor...", id);

        CityEntity city = cityRepository.findById(id)
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Güncellenecek aktif şehir bulunamadı! ID: " + id));

        CountryEntity country = countryRepository.findById(updateDto.countryId())
                .filter(CountryEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Bağlanmaya çalışılan ülke bulunamadı veya pasif! ID: " + updateDto.countryId()));

        //Plaka değişiyorsa, yeni plaka başkasında var mı?
        if (!city.getPlateCode().equalsIgnoreCase(updateDto.plateCode())) {
            Optional<CityEntity> existing = cityRepository.findByPlateCodeAndIsActiveTrue(updateDto.plateCode());
            if (existing.isPresent()) {
                throw new RuntimeException("Bu plaka koduna sahip başka bir şehir zaten var: " + updateDto.plateCode());
            }
        }

        cityMapper.updateEntityFromDto(updateDto, city, country);
        CityEntity updatedCity = cityRepository.save(city);

        return cityMapper.toDto(updatedCity);
    }

    @Override
    public CityDto getCityById(Long id) {
        log.info("{} ID'li aktif şehir aranıyor...", id);
        CityEntity city = cityRepository.findById(id)
                .filter(CityEntity::getIsActive)
                .orElseThrow(() -> new RuntimeException("Aktif şehir bulunamadı! ID: " + id));
        return cityMapper.toDto(city);
    }
}