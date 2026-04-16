package com.flashkart.userservice.service;

import com.flashkart.userservice.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID userId);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    void deleteUser(UUID userId);

    boolean existsByEmail(String email);

    // Additional methods used by controller
    List<UserResponse> getAllUsers();

    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UpdateUserRequest request);


    public record UpdateUserRequest(
            String firstName,
            String lastName,
            String phoneNumber
    ) {}
}
