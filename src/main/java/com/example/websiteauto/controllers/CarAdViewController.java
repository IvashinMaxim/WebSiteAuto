package com.example.websiteauto.controllers;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.dto.enums.CarAdSortOrder;
import com.example.websiteauto.dto.mapper.UserMapper;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdListResponse;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.dto.response.UserResponse;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.entity.enums.*;
import com.example.websiteauto.security.CustomUserDetails;
import com.example.websiteauto.service.CarAdService;
import com.example.websiteauto.service.CarService;
import com.example.websiteauto.util.PaginationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CarAdViewController {
    private static final Logger logger = LoggerFactory.getLogger(CarAdViewController.class);
    private final CarAdService carAdService;
    private final CarService carService;
    private final UserMapper userMapper;

    @GetMapping("/create")
    public String showCreateAdForm(Model model) {
        CarAdRequest emptyAdRequest = new CarAdRequest();

        model.addAttribute("ad", emptyAdRequest);
        model.addAttribute("brands", carService.getAllBrands());
        return "create-ad";
    }

    @PostMapping("/create")
    public String processCreateForm(@ModelAttribute("ad") @Valid CarAdRequest carAdRequest,
                                    BindingResult result,
                                    @RequestParam("images") List<MultipartFile> images,
                                    Model model,
                                    @AuthenticationPrincipal CustomUserDetails userDetails,
                                    RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                if (error instanceof FieldError fieldError) {
                    logger.warn("Validation new Error: Field '{}' failed with message: {}",
                            fieldError.getField(), fieldError.getDefaultMessage());
                } else {
                    logger.warn("Validation new Error: Object '{}' failed with message: {}",
                            error.getObjectName(), error.getDefaultMessage());
                }
            });
            model.addAttribute("ad", carAdRequest);
            return "create-ad";
        }
        Long authorId = userDetails.getId();
        carAdService.createCarAd(carAdRequest, authorId, images);
        redirectAttributes.addFlashAttribute("successMessage", "Объявление успешно создано");
        return "redirect:/ads";
    }


    @GetMapping
    public String listAds(@ModelAttribute CarAdFilter filter,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "12") int size,
                          @RequestParam(defaultValue = "POPULAR") CarAdSortOrder sort,
                          Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.getDirection(), sort.getProperty()));
        Page<CarAdListResponse> ads = carAdService.search(filter, pageable);

        var pagination = PaginationUtils.calculate(
                ads.getNumber(),
                ads.getTotalPages(),
                3
        );

        model.addAttribute("ads", ads.getContent());
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", pagination.currentPage);
        model.addAttribute("totalPages", pagination.totalPages);
        model.addAttribute("startPage", pagination.startPage);
        model.addAttribute("endPage", pagination.endPage);

        model.addAttribute("sortOrders", CarAdSortOrder.values());
        model.addAttribute("currentSort", sort);

        model.addAttribute("brands", carService.getAllBrands());
        if (filter.getBrand() != null && !filter.getBrand().isEmpty()) {
            model.addAttribute("models", carService.getModelsByBrand(filter.getBrand()));
        } else {
            model.addAttribute("models", Collections.emptyList());
        }
        model.addAttribute("years", carService.getAllYears());
        model.addAttribute("filter", filter);
        return "ads-list";
    }

    @GetMapping("/{id}")
    public String showAdById(@PathVariable Long id, Model model) {
        CarAdResponse carAdResponse = carAdService.getCarAdResponse(id);
        model.addAttribute("ad", carAdResponse);
        return "ad-details";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userDetails.getUser();
        UserResponse userDto = userMapper.userToUserResponse(user);
        Page<CarAdResponse> ads = carAdService.findAdsByAuthorId(user.getId(), pageable);
        model.addAttribute("currentPage", page);
        model.addAttribute("user", userDto);
        model.addAttribute("carAds", ads);
        model.addAttribute("totalPages", ads.getTotalPages());
        return "profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteAd(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        carAdService.deleteCarAd(id, userDetails.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Объявление успешно удалено");
        return "redirect:/ads/profile";
    }

    @GetMapping("/edit/{id}")
    public String editAdForm(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) throws AccessDeniedException {
        CarAdRequest adRequest = carAdService.getCarAdForEdit(id, userDetails.getId());
        model.addAttribute("ad", adRequest);
        model.addAttribute("id", id);
        model.addAttribute("images", carAdService.getCarAdEntityById(id).getImages());
        return "edit-ad";
    }

    @PostMapping("/edit/{id}")
    public String updateAd(@PathVariable Long id,
                           @Valid @ModelAttribute("ad") CarAdRequest request,
                           BindingResult bindingResult,
                           @RequestParam(value = "images") List<MultipartFile> newImages,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model,
                           RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                if (error instanceof FieldError fieldError) {
                    logger.warn("Validation Error: Field '{}' failed with message: {}",
                            fieldError.getField(), fieldError.getDefaultMessage());
                } else {
                    logger.warn("Validation Error: Object '{}' failed with message: {}",
                            error.getObjectName(), error.getDefaultMessage());
                }
            });
            model.addAttribute("images", carAdService.getCarAdEntityById(id).getImages());
            model.addAttribute("ad", request);
            model.addAttribute("id", id);
            return "edit-ad";
        }
        carAdService.updateCarAd(id, request, newImages, userDetails.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Объявление успешно обновлено");
        return "redirect:/ads/profile";
    }

    @ModelAttribute
    public void addEnumsToModel(Model model) {
        model.addAttribute("bodyTypes", BodyType.values());
        model.addAttribute("engineTypes", EngineType.values());
        model.addAttribute("driveTypes", DriveType.values());
        model.addAttribute("transmission", Transmission.values());
        model.addAttribute("steeringSide", SteeringSide.values());
    }
}

