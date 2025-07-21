package com.example.websiteauto.service;

import com.example.websiteauto.dto.request.UserRegistrationRequest;
import com.example.websiteauto.dto.request.UserRequest;
import com.example.websiteauto.dto.response.UserResponse;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.exception.EmailExistsException;
import com.example.websiteauto.exception.UserNotFoundException;
import com.example.websiteauto.repositories.UserRepository;
import com.example.websiteauto.security.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepo.existsByEmail(request.email())) {
            throw new EmailExistsException(request.email());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        User savedUser = userRepo.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("User logged in: " + authUser.getUsername());

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }


    @Transactional
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

}
