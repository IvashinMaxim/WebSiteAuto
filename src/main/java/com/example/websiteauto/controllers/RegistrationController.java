package com.example.websiteauto.controllers;

import com.example.websiteauto.dto.request.UserRegistrationRequest;
import com.example.websiteauto.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationRequest("", "", ""));
        return "register";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute("user") @Valid UserRegistrationRequest request,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        userService.registerUser(request);
        return "redirect:/ads";
    }
}
