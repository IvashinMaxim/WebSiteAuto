package com.example.websiteauto.controllers.rest;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.repositories.specification.CarAdSpecification;
import com.example.websiteauto.service.CarAdService;
import com.example.websiteauto.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class DictionaryController {
    private final CarService carService;

    @GetMapping("/dictionary")
    public List<?> getDictionary(
            @RequestParam String target,
            @RequestParam Map<String, String> params
    ) {
        return carService.searchDictionary(target, params);
    }
}
