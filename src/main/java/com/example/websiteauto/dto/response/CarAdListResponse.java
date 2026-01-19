package com.example.websiteauto.dto.response;

import java.math.BigDecimal;

public record CarAdListResponse(
        Long id,
        String title,
        BigDecimal price,
        Integer mileage,
        String city,
        String region,
        CarDtoResponse car,
        Long authorId,
        String authorUsername
) {}

