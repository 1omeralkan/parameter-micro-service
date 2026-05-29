package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.CoverageParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoverageParameterRepository extends JpaRepository<CoverageParameterEntity, Long> {
    Optional<CoverageParameterEntity> findByCoverageCodeAndIsActiveTrue(String coverageCode);
}