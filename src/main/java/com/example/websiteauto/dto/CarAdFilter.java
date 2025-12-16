package com.example.websiteauto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarAdFilter {

    private String brand;
    private String model;

    private Integer minYear;
    private Integer maxYear;

    private Integer minMileage;
    private Integer maxMileage;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private String keyword;
}