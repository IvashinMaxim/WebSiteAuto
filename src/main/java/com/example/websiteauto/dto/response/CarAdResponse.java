package com.example.websiteauto.dto.response;

import com.example.websiteauto.dto.ImageDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CarAdResponse(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        BigDecimal price,
        Integer views,
        Integer mileage,
        String color,
        String notes,
        String owner,
        String numberOfOwners,
        String city,
        String region,
        String macroRegion,
        CarDtoResponse car,
        Long authorId,
        String authorUsername,
        List<ImageDto> images
) {
}
