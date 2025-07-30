package com.example.websiteauto.dto.response;

import com.example.websiteauto.dto.CarDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CarAdResponse(
        Long id,
        String title,
        String description,
        CarDto car,
        int mileage,
        BigDecimal price,
        LocalDateTime createdAt,
        Long authorId,
        String authorUsername,
        List<String>imagePaths
) {
}
