package com.example.websiteauto.service;

import com.example.websiteauto.dto.mapper.UserMapper;
import com.example.websiteauto.dto.request.UserRegistrationRequest;
import com.example.websiteauto.dto.response.UserResponse;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.exception.EmailExistsException;
import com.example.websiteauto.exception.UserNotFoundException;
import com.example.websiteauto.repositories.UserRepository;
import com.example.websiteauto.security.CustomUserDetails;
import com.example.websiteauto.security.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new EmailExistsException(request.getEmail());
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepo.save(user);

        return userMapper.userToUserResponse(savedUser);
    }


    @Transactional
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToUserResponse(user);
    }

}
