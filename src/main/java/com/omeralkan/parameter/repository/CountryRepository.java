package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {


    List<CountryEntity> findAllByIsActiveTrue();

    Optional<CountryEntity> findByIsoCodeAndIsActiveTrue(String isoCode);
}