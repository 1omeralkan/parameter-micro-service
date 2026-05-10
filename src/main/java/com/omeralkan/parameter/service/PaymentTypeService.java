package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.PaymentTypeCreateDto;
import com.omeralkan.parameter.dto.PaymentTypeDto;

import java.util.List;

public interface PaymentTypeService {

    List<PaymentTypeDto> getAllActivePaymentTypes();

    PaymentTypeDto getPaymentTypeById(Long id);

    PaymentTypeDto getPaymentTypeByCode(String code);

    PaymentTypeDto createPaymentType(PaymentTypeCreateDto createDto);

    void deletePaymentType(Long id);
}