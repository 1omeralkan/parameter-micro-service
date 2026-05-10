package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.dto.PaymentTypeCreateDto;
import com.omeralkan.parameter.dto.PaymentTypeDto;
import com.omeralkan.parameter.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-types")
@RequiredArgsConstructor
public class PaymentTypeController {

    private final PaymentTypeService paymentTypeService;

    @GetMapping
    public ResponseEntity<List<PaymentTypeDto>> getAllPaymentTypes() {
        return ResponseEntity.ok(paymentTypeService.getAllActivePaymentTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTypeDto> getPaymentTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentTypeService.getPaymentTypeById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PaymentTypeDto> getPaymentTypeByCode(@PathVariable String code) {
        return ResponseEntity.ok(paymentTypeService.getPaymentTypeByCode(code));
    }

    @PostMapping
    public ResponseEntity<PaymentTypeDto> createPaymentType(@RequestBody PaymentTypeCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentTypeService.createPaymentType(createDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentType(@PathVariable Long id) {
        paymentTypeService.deletePaymentType(id);
        return ResponseEntity.noContent().build();
    }
}