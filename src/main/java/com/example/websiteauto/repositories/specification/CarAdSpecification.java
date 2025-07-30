package com.example.websiteauto.repositories.specification;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CarAdSpecification {
    public static Specification<CarAd> withFilter(CarAdFilter filter) {
        return (root, query, cb) -> {
            Join<CarAd, Car> carJoin = root.join("car");
            Predicate predicate = cb.conjunction();

            if (filter.brand() != null && !filter.brand().isEmpty()) {
                predicate = cb.and(predicate, cb.equal(carJoin.get("brand"), filter.brand()));
            }
            if (filter.model() != null && !filter.model().isEmpty()) {
                predicate = cb.and(predicate, cb.equal(carJoin.get("model"), filter.model()));
            }
            if (filter.minYear() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(carJoin.get("year"), filter.minYear()));
            }
            if (filter.maxYear() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(carJoin.get("year"), filter.maxYear()));
            }
            if (filter.minMileage() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("mileage"), filter.minMileage()));
            }
            if (filter.maxMileage() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("mileage"), filter.maxMileage()));
            }
            if (filter.minPrice() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), filter.minPrice()));
            }
            if (filter.maxPrice() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), filter.maxPrice()));
            }
            if (filter.keyword() != null && !filter.keyword().isEmpty()) {
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("title")), "%" + filter.keyword().toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("description")), "%" + filter.keyword().toLowerCase() + "%")
                ));
            }

            return predicate;
        };
    }

}
