package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.TownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<TownEntity, Long> {

    // SADECE BELİRLİ BİR ŞEHRE AİT aktif ilçeleri getir (Örn: Sadece İstanbul'un ilçeleri)
    List<TownEntity> findAllByCityIdAndIsActiveTrue(Long cityId);

    Optional<TownEntity> findByNameAndCityIdAndIsActiveTrue(String name, Long cityId);
}