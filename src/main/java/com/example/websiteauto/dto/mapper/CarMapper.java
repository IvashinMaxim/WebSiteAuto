package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.request.CarDtoRequest;
import com.example.websiteauto.dto.response.CarDtoResponse;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.enums.*;
import org.mapstruct.*;
import com.example.websiteauto.entity.converters.ConvertableEnum;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "bodyType", expression = "java(car.getBodyType() != null ? car.getBodyType().getValue() : null)")
    @Mapping(target = "engineType", expression = "java(car.getEngineType() != null ? car.getEngineType().getValue() : null)")
    @Mapping(target = "driveType", expression = "java(car.getDriveType() != null ? car.getDriveType().getValue() : null)")
    @Mapping(target = "transmission", expression = "java(car.getTransmission() != null ? car.getTransmission().getValue() : null)")
    @Mapping(target = "steeringSide", expression = "java(car.getSteeringSide() != null ? car.getSteeringSide().getValue() : null)")
    CarDtoResponse toCarDtoResponse(Car car);

    @Mapping(target = "bodyType", expression = "java(car.getBodyType() != null ? car.getBodyType().getValue() : null)")
    @Mapping(target = "engineType", expression = "java(car.getEngineType() != null ? car.getEngineType().getValue() : null)")
    @Mapping(target = "driveType", expression = "java(car.getDriveType() != null ? car.getDriveType().getValue() : null)")
    @Mapping(target = "transmission", expression = "java(car.getTransmission() != null ? car.getTransmission().getValue() : null)")
    @Mapping(target = "steeringSide", expression = "java(car.getSteeringSide() != null ? car.getSteeringSide().getValue() : null)")
    CarDtoRequest toDtoRequest(Car car);

    @Mapping(target = "bodyType", source = "bodyType", qualifiedByName = "mapBodyType")
    @Mapping(target = "engineType", source = "engineType", qualifiedByName = "mapEngineType")
    @Mapping(target = "driveType", source = "driveType", qualifiedByName = "mapDriveType")
    @Mapping(target = "transmission", source = "transmission", qualifiedByName = "mapTransmission")
    @Mapping(target = "steeringSide", source = "steeringSide", qualifiedByName = "mapSteeringSide")
    Car toCar(CarDtoRequest carDtoRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "bodyType", source = "bodyType", qualifiedByName = "mapBodyType")
    @Mapping(target = "engineType", source = "engineType", qualifiedByName = "mapEngineType")
    @Mapping(target = "driveType", source = "driveType", qualifiedByName = "mapDriveType")
    @Mapping(target = "transmission", source = "transmission", qualifiedByName = "mapTransmission")
    @Mapping(target = "steeringSide", source = "steeringSide", qualifiedByName = "mapSteeringSide")
    void updateCarFromDto(CarDtoRequest carDtoRequest, @MappingTarget Car car);


    @Named("mapBodyType")
    default BodyType mapBodyType(String bodyType) {
        return toEnum(BodyType.class, bodyType);
    }

    @Named("mapEngineType")
    default EngineType mapEngineType(String engineType) {
        return toEnum(EngineType.class, engineType);
    }

    @Named("mapDriveType")
    default DriveType mapDriveType(String driveType) {
        return toEnum(DriveType.class, driveType);
    }

    @Named("mapTransmission")
    default Transmission mapTransmission(String transmission) {
        return toEnum(Transmission.class, transmission);
    }

    @Named("mapSteeringSide")
    default SteeringSide mapSteeringSide(String steeringSide) {
        return toEnum(SteeringSide.class, steeringSide);
    }

    private <T extends Enum<T> & ConvertableEnum> T toEnum(Class<T> enumClass, String value) {
        if (value == null) {
            return null;
        }

        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.getValue().equalsIgnoreCase(value)) {
                return enumValue;
            }
        }

        throw new IllegalArgumentException("Неизвестное значение для " + enumClass.getSimpleName() + ": " + value);
    }

}