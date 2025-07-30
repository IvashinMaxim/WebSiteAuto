package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.CarDto;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarAdMapper {


    public CarAdResponse mapToAdResponse(CarAd carAd) {
        CarDto carDto = carAd.getCar() != null ? new CarDto(
                carAd.getCar().getBrand(),
                carAd.getCar().getModel(),
                carAd.getCar().getYear(),
                carAd.getCar().getBodyType(),
                carAd.getCar().getEngineType(),
                carAd.getCar().getEnginePower(),
                carAd.getCar().getHorsePower(),
                carAd.getCar().getDriveType(),
                carAd.getCar().getColor()
        ) : null;

        return new CarAdResponse(
                carAd.getId(),
                carAd.getTitle(),
                carAd.getDescription(),
                carDto,
                carAd.getMileage(),
                carAd.getPrice(),
                carAd.getCreatedAt(),
                carAd.getAuthor() != null ? carAd.getAuthor().getId() : null,
                carAd.getAuthor() != null ? carAd.getAuthor().getUsername() : null,
                carAd.getImagePaths() != null ? carAd.getImagePaths() : List.of()
        );
    }

    public CarAd mapToEntity(CarAdRequest request) {
        CarAd ad = new CarAd();
        ad.setTitle(request.title());
        ad.setDescription(request.description());
        ad.setMileage(request.mileage());
        ad.setPrice(request.price());
        return ad;
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
