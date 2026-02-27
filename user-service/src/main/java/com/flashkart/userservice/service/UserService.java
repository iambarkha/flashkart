package com.flashkart.userservice.service;

import com.flashkart.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserById(Long userId);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long userId, UserResponse userResponse);

    void deleteUser(Long userId);

    boolean existsByEmail(String email);

    // Additional methods used by controller
    List<UserResponse> getAllUsers();

    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UserResponse userResponse);
}
