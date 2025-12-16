package com.example.websiteauto.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDtoRequest {

    @NotBlank(message = "Бренд обязателен")
    @Size(max = 50)
    private String brand;

    @NotBlank(message = "Модель обязательна")
    @Size(max = 50)
    private String model;

    @NotNull(message = "Начальный год обязателен")
    @Min(value = 1900, message = "Год должен быть не ранее 1900")
    private Integer yearLow;

    @NotNull(message = "Конечный год обязателен")
    @Min(value = 1900, message = "Год должен быть не ранее 1900")
    @Max(value = 2100, message = "Год слишком далек")
    private Integer yearUpp;

    private Integer generation;

    private Integer restyling;

    @NotBlank(message = "Тип кузова обязателен")
    private String bodyType;

    @NotNull(message = "Объем двигателя обязателен")
    @PositiveOrZero(message = "Объем двигателя не может быть отрицательным")
    private BigDecimal engineVolume;

    @NotNull(message = "Мощность обязательна")
    @Positive(message = "Мощность должна быть положительной")
    private Integer enginePower;

    @NotBlank(message = "Тип двигателя обязателен")
    private String engineType;

    @NotBlank(message = "Трансмиссия обязательна")
    private String transmission;

    @NotBlank(message = "Тип привода обязателен")
    private String driveType;

    @NotBlank(message = "Сторона руля обязательна")
    private String steeringSide;

    private String configuration;

    private Integer configYearLow;

    private Integer configYearUpp;
}