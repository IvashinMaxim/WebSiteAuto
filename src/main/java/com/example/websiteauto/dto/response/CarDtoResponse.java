package com.example.websiteauto.dto.response;

import com.example.websiteauto.entity.enums.*;
import lombok.Value;

import java.math.BigDecimal;


public record CarDtoResponse(
        Integer carId,
        String brand,
        String model,
        Integer yearLow,
        Integer yearUpp,
        Integer generation,
        Integer restyling,
        String bodyType,
        BigDecimal engineVolume,
        Integer enginePower,
        String engineType,
        String transmission,
        String driveType,
        String steeringSide,
        String configuration) {
}
