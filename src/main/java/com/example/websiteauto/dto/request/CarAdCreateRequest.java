package com.example.websiteauto.dto.request;

import com.example.websiteauto.dto.CarDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;

public record CarAdCreateRequest(
        @NotBlank @Size(max = 100) String title,
        @Size(max = 1000) String description,
        @NotNull @Valid CarDto car,
        @Positive int mileage,
        @Positive BigDecimal price
) {}
