package com.flashkart.userservice.mapper;

import com.flashkart.userservice.dto.request.RegisterRequest;
import com.flashkart.userservice.dto.response.UserResponse;
import com.flashkart.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(RegisterRequest registerRequest);
}

