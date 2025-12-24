package com.example.websiteauto.dto;

import com.example.websiteauto.entity.enums.BodyType;
import com.example.websiteauto.entity.enums.DriveType;
import com.example.websiteauto.entity.enums.SteeringSide;
import com.example.websiteauto.entity.enums.Transmission;

import java.math.BigDecimal;

public record CarRegressionRow(
        BigDecimal price,
        Integer mileage,
        Integer enginePower,
        //BigDecimal engineVolume,

        Double year,

        BodyType bodyType,

        String color,
        String ownerInfo,     // сюда пойдет столбец ca.owner
        String notes,
        Transmission transmission,
        DriveType driveType,
        SteeringSide steeringSide,
        String macroregion,
        String city // это поле "owner" из базы
) {}
