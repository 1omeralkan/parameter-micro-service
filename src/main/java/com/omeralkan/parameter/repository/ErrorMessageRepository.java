package com.omeralkan.parameter.repository;

import com.omeralkan.parameter.entity.ErrorMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessageEntity, Long> {

    Optional<ErrorMessageEntity> findByErrorCodeAndLanguage(String errorCode, String language);
}