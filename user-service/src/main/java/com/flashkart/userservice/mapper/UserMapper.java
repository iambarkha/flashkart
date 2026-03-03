package com.flashkart.userservice.mapper;

import com.flashkart.userservice.dto.request.RegisterRequest;
import com.flashkart.userservice.dto.response.UserResponse;
import com.flashkart.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest registerRequest);
}

