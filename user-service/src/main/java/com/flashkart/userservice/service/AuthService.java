package com.flashkart.userservice.service;

import com.flashkart.userservice.dto.request.LoginRequest;
import com.flashkart.userservice.dto.request.RegisterRequest;
import com.flashkart.userservice.dto.response.AuthResponse;

import java.util.UUID;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);
Ø
}

