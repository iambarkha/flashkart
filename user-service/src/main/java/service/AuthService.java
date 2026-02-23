package service;

import dto.AuthResponse;
import dto.LoginRequest;
import dto.RegisterRequest;
import dto.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    UserResponse getUserProfile(Long userId);

    UserResponse updateUserProfile(Long userId, UserResponse userResponse);

    void deleteUser(Long userId);
}

