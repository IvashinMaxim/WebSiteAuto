package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.response.UserResponse;
import com.example.websiteauto.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}

