package com.example.websiteauto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


public record CarAdFilter(String brand, String model, Integer minYear, Integer maxYear,
                          Integer minMileage, Integer maxMileage, BigDecimal minPrice, BigDecimal maxPrice,
                          String keyword){}

