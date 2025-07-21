package com.example.websiteauto.repositories.specification;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.entity.CarAd;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CarAdSpecification {
    public static Specification<CarAd> withFilter(CarAdFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getBrand() != null && !filter.getBrand().isEmpty()) {
                predicates.add(cb.equal(root.get("car").get("brand"), filter.getBrand()));
            }
            if (filter.getModel() != null && !filter.getModel().isEmpty()) {
                predicates.add(cb.equal(root.get("car").get("model"), filter.getModel()));
            }
            if (filter.getMinYear() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("car").get("year"), filter.getMinYear()));
            }
            if (filter.getMaxYear() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("car").get("year"), filter.getMaxYear()));
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
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
