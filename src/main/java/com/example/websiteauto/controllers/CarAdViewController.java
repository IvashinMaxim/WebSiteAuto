package com.example.websiteauto.controllers;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.dto.CarDto;
import com.example.websiteauto.dto.mapper.CarAdMapper;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.entity.enums.BodyType;
import com.example.websiteauto.entity.enums.DriveType;
import com.example.websiteauto.entity.enums.EngineType;
import com.example.websiteauto.security.CustomUserDetails;
import com.example.websiteauto.service.CarAdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CarAdViewController {

    private final CarAdService carAdService;
    private final CarAdMapper carAdMapper;

    @GetMapping("/create")
    public String showCreateAdForm(Model model) {
        CarDto emptyCar = new CarDto(
                "", "", 0,
                null, null, 0.0, 0,
                null, ""
        );

        CarAdRequest emptyAd = new CarAdRequest(
                "", "", emptyCar, 0, BigDecimal.ZERO
        );

        model.addAttribute("carAd", emptyAd);
        model.addAttribute("bodyTypes", BodyType.values());
        model.addAttribute("engineTypes", EngineType.values());
        model.addAttribute("driveTypes", DriveType.values());

        return "create-ad";
    }

    @PostMapping("/create")
    public String processCreateForm(@ModelAttribute("carAd") @Valid CarAdRequest carAdRequest,
                                    BindingResult result,
                                    Model model,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            System.out.println("Errors: " + result.getAllErrors());
            model.addAttribute("bodyTypes", BodyType.values());
            model.addAttribute("engineTypes", EngineType.values());
            model.addAttribute("driveTypes", DriveType.values());
            model.addAttribute("carAd", carAdRequest);
            return "create-ad";
        }

        Long authorId = userDetails.getId();
        carAdService.createCarAd(carAdRequest, authorId);

        return "redirect:/ads";
    }


    @GetMapping
    public String listAds(@ModelAttribute CarAdFilter filter, Model model) {
        List<CarAdResponse> ads = carAdService.search(filter);

        model.addAttribute("ads", ads);
        model.addAttribute("brands", carAdService.getAllBrands());
        model.addAttribute("models", carAdService.getAllModels());
        model.addAttribute("years", carAdService.getAllYears());
        model.addAttribute("filter", filter);
        return "ads-list";
    }

    @GetMapping("/{id}")
    public String showAdById(@PathVariable Long id, Model model) {
        CarAdResponse carAdResponse = carAdService.getCarAdById(id);
        model.addAttribute("carAd", carAdResponse);
        return "ad-details";
    }

//    @GetMapping("/ads/edit/{id}")
//    public String editAdForm(@PathVariable Long id, Model model) {
//        CarAdResponse carAd=carAdService.getCarAdById(id);
//        model.addAttribute("carAd", carAd);
//        model.addAttribute("bodyTypes", BodyType.values());
//        model.addAttribute("engineTypes", EngineType.values());
//        model.addAttribute("driveTypes", DriveType.values());
//        return "edit-ad";
//    }
//
//    @PostMapping("/ads/edit/{id}")
//    public String updateAd(@PathVariable Long id,
//                           @ModelAttribute("carAd") @Valid CarAdRequest carAdRequest,
//                           BindingResult bindingResult,
//                           RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "edit-ad";
//        }
//
//        carAdService.updateCarAd(id, carAdRequest);
//        redirectAttributes.addFlashAttribute("success", "Объявление обновлено!");
//        return "redirect:/ads";
//    }



}
