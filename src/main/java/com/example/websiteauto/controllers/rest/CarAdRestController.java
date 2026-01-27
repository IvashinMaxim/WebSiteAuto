package com.example.websiteauto.controllers.rest;

import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdListResponse;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.security.CustomUserDetails;
import com.example.websiteauto.service.CarAdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.websiteauto.dto.CarAdFilter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class CarAdRestController {

    private final CarAdService carAdService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarAdResponse create(
            @Valid @RequestBody CarAdRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long id = carAdService.createCarAd(request, user.getId());
        return carAdService.getCarAdResponse(id);
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> uploadImages(
            @PathVariable Long id,
            @RequestPart("images") List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException {
        return carAdService.addImages(id, user.getId(), images);
    }

    @GetMapping
    public Page<CarAdListResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            CarAdFilter filter
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return carAdService.search(filter, pageable);
    }

    @GetMapping("/{id}")
    public CarAdResponse getById(@PathVariable Long id) {
        return carAdService.getCarAdResponse(id);
    }

    @PatchMapping("/{id}")
    public CarAdResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CarAdRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        carAdService.updateCarAd(id, request, user.getId());
        return carAdService.getCarAdResponse(id);
    }

    @DeleteMapping("/{id}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @PathVariable Long id,
            @PathVariable Long imageId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        carAdService.deleteImage(id, imageId, user.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        carAdService.deleteCarAd(id, user.getId());
    }
}
