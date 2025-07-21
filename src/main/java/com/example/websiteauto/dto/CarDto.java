package com.example.websiteauto.dto;

import com.example.websiteauto.entity.enums.BodyType;
import com.example.websiteauto.entity.enums.DriveType;
import com.example.websiteauto.entity.enums.EngineType;
import jakarta.validation.constraints.*;

public record CarDto(
        @NotBlank(message = "Марка обязательна")
        @Size(max = 50, message = "Марка не должна превышать 50 символов")
        String brand,

        @NotBlank(message = "Модель обязательна")
        @Size(max = 50, message = "Модель не должна превышать 50 символов")
        String model,

        @Max(value = 2025,message = "Год не может быть позже текущего года")
        @PositiveOrZero(message = "Год должен быть положительным")
        int year,

        BodyType bodyType,
        EngineType engineType,

        @Positive(message = "Engine power must be positive")
        double enginePower,

        @Positive(message = "Horsepower must be positive")
        int horsePower,

        DriveType driveType,
        String color
) {}
