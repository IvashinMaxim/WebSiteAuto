package com.example.websiteauto.controllers;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.dto.CarDto;
import com.example.websiteauto.dto.mapper.UserMapper;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.dto.response.UserResponse;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.entity.enums.BodyType;
import com.example.websiteauto.entity.enums.DriveType;
import com.example.websiteauto.entity.enums.EngineType;
import com.example.websiteauto.security.CustomUserDetails;
import com.example.websiteauto.service.CarAdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CarAdViewController {

    private final CarAdService carAdService;
    private final UserMapper userMapper;

    @GetMapping("/create")
    public String showCreateAdForm(Model model) {
        CarDto emptyCar = new CarDto("", "", 0, null, null, 0.0, 0, null, "");
        CarAdRequest emptyAd = new CarAdRequest("", "", emptyCar, 0, BigDecimal.ZERO);
        model.addAttribute("carAd", emptyAd);
        model.addAttribute("bodyTypes", BodyType.values());
        model.addAttribute("engineTypes", EngineType.values());
        model.addAttribute("driveTypes", DriveType.values());
        return "create-ad";
    }

    @PostMapping("/create")
    public String processCreateForm(@ModelAttribute("carAd") @Valid CarAdRequest carAdRequest,
                                    BindingResult result,
                                    @RequestParam("images") List<MultipartFile> images,
                                    Model model,
                                    @AuthenticationPrincipal CustomUserDetails userDetails,
                                    RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("bodyTypes", BodyType.values());
            model.addAttribute("engineTypes", EngineType.values());
            model.addAttribute("driveTypes", DriveType.values());
            model.addAttribute("carAd", carAdRequest);
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
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarAdResponse> ads = carAdService.search(filter, pageable);

        model.addAttribute("ads", ads.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ads.getTotalPages());
        model.addAttribute("brands", carAdService.getAllBrands());
        model.addAttribute("models", carAdService.getAllModels());
        model.addAttribute("years", carAdService.getAllYears());
        model.addAttribute("filter", filter);
        return "ads-list";
    }

    @GetMapping("/{id}")
    public String showAdById(@PathVariable Long id, Model model) {
        CarAdResponse carAdResponse = carAdService.getCarAdResponseById(id);
        model.addAttribute("carAd", carAdResponse);
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
                           RedirectAttributes redirectAttributes) throws AccessDeniedException {
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
        return "edit-ad";
    }

    @PostMapping("/edit/{id}")
    public String updateAd(@PathVariable Long id,
                           @Valid @ModelAttribute("ad") CarAdRequest request,
                           BindingResult bindingResult,
                           @RequestParam("images") List<MultipartFile> images,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model,
                           RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("brands", carAdService.getAllBrands());
            model.addAttribute("models", carAdService.getAllModels());
            model.addAttribute("years", carAdService.getAllYears());
            return "edit-ad";
        }

        carAdService.updateCarAd(id, request, images, userDetails.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Объявление успешно обновлено");
        return "redirect:/ads/profile";
    }
}

