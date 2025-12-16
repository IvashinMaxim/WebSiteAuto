package com.example.websiteauto.controllers.rest;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.repositories.specification.CarAdSpecification;
import com.example.websiteauto.service.CarAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class DictionaryController {
    private final CarAdService carAdService;

    @GetMapping("/dictionary")
    public List<Object> getDictionaryValues(
            @RequestParam String target,
            @ModelAttribute CarAdFilter filter
    ) {
        Specification<CarAd> spec = CarAdSpecification.withFilter(filter);
        return carAdService.getDistinctValues(target, spec);
    }
}
