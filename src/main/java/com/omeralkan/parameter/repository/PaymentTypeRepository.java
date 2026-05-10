package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Long> {

    List<PaymentTypeEntity> findAllByIsActiveTrue();

    Optional<PaymentTypeEntity> findByCodeAndIsActiveTrue(String code);

    Optional<PaymentTypeEntity> findByIdAndIsActiveTrue(Long id);
}