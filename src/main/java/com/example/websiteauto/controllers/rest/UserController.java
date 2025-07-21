//package com.example.websiteauto.controllers;
//
//import com.example.websiteauto.dto.request.UserRequest;
//import com.example.websiteauto.dto.response.UserResponse;
//import com.example.websiteauto.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
//
//    @PostMapping("/register")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponse registerUser(@RequestBody @Valid UserRequest request) {
//        return userService.registerUser(request);
//    }
//}
