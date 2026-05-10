package com.omeralkan.parameter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_types")
public class PaymentTypeEntity extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "min_installment", nullable = false)
    private Integer minInstallment;

    @Column(name = "max_installment", nullable = false)
    private Integer maxInstallment;
}