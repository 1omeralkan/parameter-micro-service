package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    // Kaan abinin "tetikte olun" kuralı: Silinmişleri (pasifleri) getirme!
    List<CountryEntity> findAllByIsActiveTrue();

    // ISO koduna göre (Örn: TR) aktif ülkeyi bulma
    Optional<CountryEntity> findByIsoCodeAndIsActiveTrue(String isoCode);
}