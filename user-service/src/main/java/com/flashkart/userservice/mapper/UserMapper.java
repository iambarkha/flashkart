package com.flashkart.userservice.mapper;

import com.flashkart.userservice.dto.RegisterRequest;
import com.flashkart.userservice.dto.UserResponse;
import com.flashkart.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest registerRequest);
}

