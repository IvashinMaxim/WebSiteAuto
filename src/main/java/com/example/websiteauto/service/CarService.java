package com.example.websiteauto.service;

import com.example.websiteauto.dto.mapper.CarMapper;
import com.example.websiteauto.dto.request.CarDtoRequest;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.converters.ConvertableEnum;
import com.example.websiteauto.entity.enums.*;
import com.example.websiteauto.repositories.CarRepository;
import jakarta.persistence.EntityManager;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarService {
    private static final Logger log = LoggerFactory.getLogger(CarService.class);


    private final CarRepository carRepository;
    private final CarMapper carMapper;

    private final EntityManager em;

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

    @Cacheable("brands")
    public List<String> getAllBrands() {
        return carRepository.findDistinctBrands();
    }

    @Cacheable("years")
    public List<Integer> getAllYears() {
        return carRepository.findDistinctYears();
    }

    @Cacheable("models")
    public List<String> getAllModels() {
        return carRepository.findDistinctModels();
    }

    @Cacheable(value = "modelsByBrand", key = "#brand")
    public List<String> getModelsByBrand(String brand) {
        return carRepository.findModelsByBrand(brand);
    }

    @Transactional(readOnly = true)
    public List<?> searchDictionary(String target, Map<String, String> params) {
        if ("color".equals(target)) {
            return List.of("Белый", "Черный", "Серый", "Серебристый", "Синий", "Красный", "Зеленый", "Коричневый", "Бежевый", "Желтый");
        }
        log.debug(
                "Dictionary search. target={}, paramsKeys={}",
                target,
                params.keySet()
        );


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Car> root = query.from(Car.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("brand") && !params.get("brand").isEmpty()) {
            predicates.add(cb.equal(root.get("brand"), params.get("brand")));
        }
        if (params.containsKey("model") && !params.get("model").isEmpty()) {
            predicates.add(cb.equal(root.get("model"), params.get("model")));
        }
        if (params.containsKey("yearLow") && !params.get("yearLow").isEmpty()) {
            try {
                int selectedYear = Integer.parseInt(params.get("yearLow"));
                Predicate startOk = cb.lessThanOrEqualTo(root.get("yearLow"), (short) selectedYear);
                Predicate endOk = cb.or(
                        root.get("yearUpp").isNull(),
                        cb.greaterThanOrEqualTo(root.get("yearUpp"), (short) selectedYear)
                );
                predicates.add(cb.and(startOk, endOk));
            } catch (NumberFormatException ignored) {
            }
        }
        if (params.containsKey("generation") && !params.get("generation").isEmpty()) {
            predicates.add(cb.equal(root.get("generation"), Byte.parseByte(params.get("generation"))));
        }
        if (params.containsKey("enginePower") && !params.get("enginePower").isEmpty()) {
            predicates.add(cb.equal(root.get("enginePower"), Integer.parseInt(params.get("enginePower"))));
        }
        if (params.containsKey("bodyType")) {
            BodyType val = parseEnum(BodyType.class, params.get("bodyType"));
            if (val != null) {
                predicates.add(cb.equal(root.get("bodyType"), val));
            }
        }

        if (params.containsKey("engineType")) {
            EngineType val = parseEnum(EngineType.class, params.get("engineType"));
            if (val != null) {
                predicates.add(cb.equal(root.get("engineType"), val));
            }
        }

        if (params.containsKey("transmissionType")) {
            Transmission val = parseEnum(Transmission.class, params.get("transmissionType"));
            if (val != null) {
                predicates.add(cb.equal(root.get("transmission"), val));
            }
        }

        if (params.containsKey("driveType")) {
            DriveType val = parseEnum(DriveType.class, params.get("driveType"));
            if (val != null) {
                predicates.add(cb.equal(root.get("driveType"), val));
            }
        }

        if (params.containsKey("steeringSide")) {
            SteeringSide val = parseEnum(SteeringSide.class, params.get("steeringSide"));
            if (val != null) {
                predicates.add(cb.equal(root.get("steeringSide"), val));
            }
        }

        if ("yearLow".equals(target)) {
            return getYearsForModel(params.get("brand"), params.get("model"));
        }

        String entityField = mapJsFieldToEntityField(target);
        query.select(root.get(entityField)).distinct(true);
        query.where(predicates.toArray(new Predicate[0]));

        query.orderBy(cb.asc(root.get(entityField)));

        List<Object> resultList = em.createQuery(query).getResultList();

        return resultList.stream()
                .map(item -> {
                    if (item instanceof ConvertableEnum convertableEnum) {
                        return convertableEnum.getValue();
                    }
                    return item;
                })
                .toList();
    }

    private String mapJsFieldToEntityField(String jsField) {
        return switch (jsField) {
            case "transmissionType" -> "transmission";
            case "enginePowerSelect", "enginePower" -> "enginePower";
            case "engineVolume" -> "engineVolume";
            case "steeringSide" -> "steeringSide";
            default -> jsField;
        };
    }

    private List<Integer> getYearsForModel(String brand, String model) {
        if (brand == null || model == null) return List.of();

        List<Object[]> range = carRepository.findYearRangeByModel(brand, model);
        if (range.isEmpty() || range.get(0)[0] == null) return List.of();

        short min = (Short) range.get(0)[0];
        short max = (range.get(0)[1] != null) ? (Short) range.get(0)[1] : (short) java.time.Year.now().getValue();

        List<Integer> years = new ArrayList<>();
        for (int i = max; i >= min; i--) {
            years.add(i);
        }
        return years;
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T> & ConvertableEnum> T parseEnum(Class<T> enumClass, String value) {
        if (value == null || value.isBlank()) return null;

        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException ignored) {
        }

        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getValue().equalsIgnoreCase(value)) {
                return enumConstant;
            }
        }

        return null;
    }
}

