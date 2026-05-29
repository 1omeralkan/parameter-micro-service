package com.omeralkan.parameter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "coverage_parameters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoverageParameterEntity extends BaseEntity {

    @Column(name = "coverage_code", nullable = false, unique = true, length = 10)
    private String coverageCode;

    @Column(name = "multiplier", nullable = false, precision = 10, scale = 2)
    private BigDecimal multiplier;

    @Column(name = "description")
    private String description;
}