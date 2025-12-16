package com.example.websiteauto.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarAdRequest {
    private Long id;

    @Valid
    @NotNull
    private CarDtoRequest car;

    @Size(max = 50)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    private BigDecimal price;

    @NotNull(message = "Пробег обязателен")
    @Positive(message = "Пробег должен быть положительным")
    private Integer mileage;

    @NotBlank(message = "Цвет обязателен")
    private String color;

    private String owner;

    private String numberOfOwners;

    @NotBlank(message = "Город обязателен")
    private String city;

    @NotBlank(message = "Регион обязателен")
    private String region;

    private String macroRegion;

    private String notes;

    private List<Long> existingImageIds;

    private List<Long> removeImageIds;
}