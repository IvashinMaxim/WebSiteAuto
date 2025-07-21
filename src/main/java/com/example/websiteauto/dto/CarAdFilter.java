package com.example.websiteauto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarAdFilter {
    private String brand;
    private String model;
    private Integer minYear;
    private Integer maxYear;
    private Integer minMileage;
    private Integer maxMileage;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}

