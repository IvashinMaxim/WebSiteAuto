//package com.example.websiteauto.controllers;
//
//import com.example.websiteauto.dto.CarAdFilter;
//import com.example.websiteauto.dto.request.CarAdRequest;
//import com.example.websiteauto.dto.response.CarAdResponse;
//import com.example.websiteauto.entity.CarAd;
//import com.example.websiteauto.repositories.CarAdRepository;
//import com.example.websiteauto.service.CarAdService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/ads")
//@RequiredArgsConstructor
//public class CarAdRestController {
//    private final CarAdService carAdService;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public CarAdResponse createAd(@RequestBody @Valid CarAdRequest request) {
//        Long authorId = 1L;
//        return carAdService.createCarAd(request, authorId);
//    }
//
//    @GetMapping
//    public List<CarAdResponse> getAllAds() {
//        return carAdService.getAllCarAds();
//    }
//
//    @GetMapping("/{id}")
//    public CarAdResponse getAdById(@PathVariable Long id) {
//        return carAdService.getCarAdById(id);
//    }
//
//    @PutMapping("/{id}")
//    public CarAdResponse updateAd(@PathVariable Long id, @RequestBody @Valid CarAdRequest request) {
//        return carAdService.updateCarAd(id, request);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteAd(@PathVariable Long id) {
//        carAdService.deleteCarAd(id);
//    }
//}
//
