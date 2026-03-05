package com.omeralkan.parameter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "towns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TownEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Birçok ilçe bir şehre aittir
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;
}