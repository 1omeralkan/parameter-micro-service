package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.RiskParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RiskParameterRepository extends JpaRepository<RiskParameterEntity, Long> {
    Optional<RiskParameterEntity> findByParamKeyAndIsActiveTrue(String paramKey);
    List<RiskParameterEntity> findAllByIsActiveTrue();
}