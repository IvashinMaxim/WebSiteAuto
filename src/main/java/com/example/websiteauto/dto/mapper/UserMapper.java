package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.request.UserRegistrationRequest;
import com.example.websiteauto.dto.request.UserUpdateRequest;
import com.example.websiteauto.dto.response.UserResponse;
import com.example.websiteauto.dto.response.UserSummaryDto;
import com.example.websiteauto.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    UserSummaryDto userToUserSummaryDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(UserRegistrationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);
}