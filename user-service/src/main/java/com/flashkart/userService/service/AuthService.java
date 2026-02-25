package com.flashkart.userService.service;

import com.flashkart.userService.dto.AuthResponse;
import com.flashkart.userService.dto.LoginRequest;
import com.flashkart.userService.dto.RegisterRequest;
import com.flashkart.userService.dto.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    UserResponse getUserProfile(Long userId);

    UserResponse updateUserProfile(Long userId, UserResponse userResponse);

    void deleteUser(Long userId);
}

