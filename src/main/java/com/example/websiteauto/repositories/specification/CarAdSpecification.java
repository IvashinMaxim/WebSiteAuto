package com.example.websiteauto.repositories.specification;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CarAdSpecification {
    public static Specification<CarAd> withFilter(CarAdFilter filter) {
        return (root, query, cb) -> {
            Join<CarAd, Car> carJoin = root.join("car");

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getBrand() != null && !filter.getBrand().isEmpty()) {
                predicates.add(cb.equal(carJoin.get("brand"), filter.getBrand()));
            }
            if (filter.getModel() != null && !filter.getModel().isEmpty()) {
                predicates.add(cb.equal(carJoin.get("model"), filter.getModel()));
            }
            if (filter.getMinYear() != null) {
                predicates.add(cb.greaterThanOrEqualTo(carJoin.get("yearLow"), filter.getMinYear()));
            }
            if (filter.getMaxYear() != null) {
                predicates.add(cb.lessThanOrEqualTo(carJoin.get("yearUpp"), filter.getMaxYear()));
            }

            if (filter.getMinMileage() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("mileage"), filter.getMinMileage()));
            }
            if (filter.getMaxMileage() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("mileage"), filter.getMaxMileage()));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
                String pattern = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}