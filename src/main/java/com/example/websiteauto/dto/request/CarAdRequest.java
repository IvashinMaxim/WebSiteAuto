package com.example.websiteauto.dto.request;

import com.example.websiteauto.dto.CarDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

public record CarAdRequest(
        @NotBlank(message = "Title is required")
        String title,
        String description,

        @NotNull(message = "Car data is required")
        @Valid
        CarDto car,

        @Positive(message = "Mileage must be positive")
        int mileage,

        @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
        @DecimalMax(value = "900000000.0", message = "Цена не может превышать 900,000,000")
        BigDecimal price
) implements Serializable {
}
