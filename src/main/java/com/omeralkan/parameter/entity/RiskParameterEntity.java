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
@Table(name = "risk_parameters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskParameterEntity extends BaseEntity {
    @Column(name = "param_key", nullable = false, unique = true, length = 50)
    private String paramKey;

    @Column(name = "param_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal paramValue;

    @Column(name = "description")
    private String description;
}