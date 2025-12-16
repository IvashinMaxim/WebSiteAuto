package com.example.websiteauto.service;

import com.example.websiteauto.dto.mapper.CarMapper;
import com.example.websiteauto.dto.request.CarDtoRequest;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public boolean carChanged(Car oldCar, CarDtoRequest dto) {
        if (!Objects.equals(oldCar.getBrand(), dto.getBrand())) return true;
        if (!Objects.equals(oldCar.getModel(), dto.getModel())) return true;

        if (!Objects.equals(oldCar.getYearLow(), safeShort(dto.getYearLow()))) return true;
        if (!Objects.equals(oldCar.getYearUpp(), safeShort(dto.getYearUpp()))) return true;

        if (!Objects.equals(oldCar.getGeneration(), safeByte(dto.getGeneration()))) return true;
        if (!Objects.equals(oldCar.getRestyling(), safeByte(dto.getRestyling()))) return true;

        if (!Objects.equals(oldCar.getBodyType().getValue(), dto.getBodyType())) return true;
        if (!Objects.equals(oldCar.getEngineType().getValue(), dto.getEngineType())) return true;
        if (!Objects.equals(oldCar.getDriveType().getValue(), dto.getDriveType())) return true;
        if (!Objects.equals(oldCar.getTransmission().getValue(), dto.getTransmission())) return true;
        if (!Objects.equals(oldCar.getSteeringSide().getValue(), dto.getSteeringSide())) return true;

        if (!Objects.equals(oldCar.getEngineVolume(), dto.getEngineVolume())) return true;
        if (!Objects.equals(oldCar.getEnginePower(), dto.getEnginePower())) return true;

        if (!Objects.equals(oldCar.getConfiguration(), dto.getConfiguration())) return true;
        if (!Objects.equals(oldCar.getConfigYearLow(), safeShort(dto.getConfigYearLow()))) return true;
        if (!Objects.equals(oldCar.getConfigYearUpp(), safeShort(dto.getConfigYearUpp()))) return true;


        return false;
    }

    private Short safeShort(Integer value) {
        return value == null ? null : value.shortValue();
    }

    private Byte safeByte(Integer value) {
        return value == null ? null : value.byteValue();
    }

}

