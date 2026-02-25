package com.flashkart.userService.mapper;

import com.flashkart.userService.dto.RegisterRequest;
import com.flashkart.userService.dto.UserResponse;
import com.flashkart.userService.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest registerRequest);
}

