package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.CarDto;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarAdMapper {
    CarAdMapper INSTANCE = Mappers.getMapper(CarAdMapper.class);

    @Mapping(target = "car", source = "car")
    CarAdResponse toAdResponse(CarAd carAd);

    @Mapping(target = "car", ignore = true)
    CarAd toEntity(CarAdRequest request);

    @Mapping(target = "car", source = "car")
    CarAdRequest toRequest(CarAd carAd);

    CarDto toCarDto(Car car);

    void updateCarFromDto(CarDto carDto, @MappingTarget Car car);

    Car toCar(CarDto carDto);
}
