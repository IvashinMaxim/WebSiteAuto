package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.CarDto;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import org.springframework.stereotype.Component;

@Component
public class CarAdMapper {

    public CarAdResponse mapToAdResponse(CarAd ad) {
        return new CarAdResponse(
                ad.getId(),
                ad.getTitle(),
                ad.getDescription(),
                mapToCarDto(ad.getCar()),
                ad.getMileage(),
                ad.getPrice(),
                ad.getCreatedAt(),
                ad.getAuthor().getId(),
                ad.getAuthor().getUsername()
        );
    }

    public CarAdRequest mapToRequest(CarAd ad) {
        return new CarAdRequest(
                ad.getTitle(),
                ad.getDescription(),
                mapToCarDto(ad.getCar()),
                ad.getMileage(),
                ad.getPrice()
        );
    }

    public CarDto mapToCarDto(Car car) {
        return new CarDto(
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getBodyType(),
                car.getEngineType(),
                car.getEnginePower(),
                car.getHorsePower(),
                car.getDriveType(),
                car.getColor()
        );
    }
}
