package com.omeralkan.parameter.mapper;

import com.omeralkan.parameter.dto.PaymentTypeCreateDto;
import com.omeralkan.parameter.dto.PaymentTypeDto;
import com.omeralkan.parameter.entity.PaymentTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentTypeMapper {

    public PaymentTypeDto toDto(PaymentTypeEntity entity) {
        return new PaymentTypeDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getMinInstallment(),
                entity.getMaxInstallment(),
                entity.getIsActive()
        );
    }

    public PaymentTypeEntity toEntity(PaymentTypeCreateDto createDto) {
        PaymentTypeEntity entity = new PaymentTypeEntity();
        entity.setCode(createDto.code());
        entity.setName(createDto.name());
        entity.setMinInstallment(createDto.minInstallment());
        entity.setMaxInstallment(createDto.maxInstallment());
        return entity;
    }
}