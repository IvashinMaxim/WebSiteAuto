package com.example.websiteauto.controllers.rest;

import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.security.CustomUserDetails;
import com.example.websiteauto.service.CarAdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class CarAdRestController {
    private final CarAdService carAdService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarAdResponse createCarAd(@RequestBody @Valid CarAdRequest request,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     @RequestParam("images") List<MultipartFile> images) throws IOException {
        Long authorId = customUserDetails.getId();
        return carAdService.createCarAd(request, authorId, images);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarAdResponse> getAllAds() {
        return carAdService.getAllCarAds();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarAdResponse getAdById(@PathVariable Long id) {
        return carAdService.getCarAdResponseById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarAdResponse updateCarAd(@PathVariable Long id, @RequestBody @Valid CarAdRequest request,
                                     @RequestParam("images") List<MultipartFile> images,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        return carAdService.updateCarAd(id, request, images, customUserDetails.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAd(@PathVariable Long id,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        carAdService.deleteCarAd(id, customUserDetails.getId());
    }


}