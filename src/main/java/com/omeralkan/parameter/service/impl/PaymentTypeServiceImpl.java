package com.omeralkan.parameter.service.impl;

import com.omeralkan.parameter.dto.PaymentTypeCreateDto;
import com.omeralkan.parameter.dto.PaymentTypeDto;
import com.omeralkan.parameter.entity.PaymentTypeEntity;
import com.omeralkan.parameter.exception.BusinessException;
import com.omeralkan.parameter.mapper.PaymentTypeMapper;
import com.omeralkan.parameter.repository.PaymentTypeRepository;
import com.omeralkan.parameter.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentTypeMapper paymentTypeMapper;

    private static final String PAYMENT_TYPE_NOT_FOUND = "PARAM-PT-404";

    @Override
    public List<PaymentTypeDto> getAllActivePaymentTypes() {
        return paymentTypeRepository.findAllByIsActiveTrue()
                .stream()
                .map(paymentTypeMapper::toDto)
                .toList();
    }

    @Override
    public PaymentTypeDto getPaymentTypeById(Long id) {
        PaymentTypeEntity entity = paymentTypeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException(PAYMENT_TYPE_NOT_FOUND));
        return paymentTypeMapper.toDto(entity);
    }

    @Override
    public PaymentTypeDto getPaymentTypeByCode(String code) {
        PaymentTypeEntity entity = paymentTypeRepository.findByCodeAndIsActiveTrue(code.toUpperCase())
                .orElseThrow(() -> new BusinessException(PAYMENT_TYPE_NOT_FOUND));
        return paymentTypeMapper.toDto(entity);
    }

    @Override
    public PaymentTypeDto createPaymentType(PaymentTypeCreateDto createDto) {
        PaymentTypeEntity entity = paymentTypeMapper.toEntity(createDto);
        PaymentTypeEntity savedEntity = paymentTypeRepository.save(entity);
        return paymentTypeMapper.toDto(savedEntity);
    }

    @Override
    public void deletePaymentType(Long id) {
        PaymentTypeEntity entity = paymentTypeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException(PAYMENT_TYPE_NOT_FOUND));
        entity.setIsActive(false);
        paymentTypeRepository.save(entity);
        log.info("Ödeme tipi pasife çekildi. ID: {}", id);
    }
}