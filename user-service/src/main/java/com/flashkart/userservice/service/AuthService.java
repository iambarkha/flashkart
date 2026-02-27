package com.flashkart.userservice.service;

import com.flashkart.userservice.dto.AuthResponse;
import com.flashkart.userservice.dto.LoginRequest;
import com.flashkart.userservice.dto.RegisterRequest;
import com.flashkart.userservice.dto.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    UserResponse getUserProfile(Long userId);

    UserResponse updateUserProfile(Long userId, UserResponse userResponse);

    void deleteUser(Long userId);
}

