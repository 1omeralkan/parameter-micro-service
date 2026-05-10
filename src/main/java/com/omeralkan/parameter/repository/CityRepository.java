package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    List<CityEntity> findAllByIsActiveTrue();

    List<CityEntity> findAllByCountryIdAndIsActiveTrue(Long countryId);

    Optional<CityEntity> findByPlateCodeAndIsActiveTrue(String plateCode);
}