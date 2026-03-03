package com.flashkart.userservice.service;

import com.flashkart.userservice.dto.request.LoginRequest;
import com.flashkart.userservice.dto.request.RegisterRequest;
import com.flashkart.userservice.dto.response.ApiResponse;
import com.flashkart.userservice.dto.response.AuthResponse;
import com.flashkart.userservice.dto.response.UserResponse;

public interface AuthService {

    ApiResponse<AuthResponse> register(RegisterRequest registerRequest);

    ApiResponse<AuthResponse> login(LoginRequest loginRequest);

    UserResponse getUserProfile(Long userId);

    UserResponse updateUserProfile(Long userId, UserResponse userResponse);

    void deleteUser(Long userId);
}

