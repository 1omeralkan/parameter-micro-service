package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    // Sadece aktif olan şehirleri getir
    List<CityEntity> findAllByIsActiveTrue();

    // SADECE BELİRLİ BİR ÜLKEYE AİT aktif şehirleri getir (Örn: Sadece Türkiye'nin şehirleri)
    List<CityEntity> findAllByCountryIdAndIsActiveTrue(Long countryId);

    // Plaka koduna göre aktif şehri bul (Örn: 34)
    Optional<CityEntity> findByPlateCodeAndIsActiveTrue(String plateCode);
}