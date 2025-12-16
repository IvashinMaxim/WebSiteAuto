package com.example.websiteauto.repositories;

import com.example.websiteauto.entity.CarAd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CarAdRepositoryCustom {
    Page<Long> findAllIdsBySpecification(Specification<CarAd> spec, Pageable pageable);

    List<Object> findDistinctValues(String fieldName, Specification<CarAd> spec);
}