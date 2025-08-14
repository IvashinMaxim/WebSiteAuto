package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.response.UserResponse;
import com.example.websiteauto.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse userToUserResponse(User user);
}

