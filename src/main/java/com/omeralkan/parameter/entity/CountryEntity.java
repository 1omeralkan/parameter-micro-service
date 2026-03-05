package com.omeralkan.parameter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "iso_code", nullable = false, length = 2, unique = true)
    private String isoCode;

    @Column(name = "phone_code", nullable = false, length = 10)
    private String phoneCode;

    // Bir ülkenin birden fazla şehri olur (One-To-Many)
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CityEntity> cities = new ArrayList<>();
}