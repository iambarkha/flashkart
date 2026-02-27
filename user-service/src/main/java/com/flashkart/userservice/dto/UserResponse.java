package com.flashkart.userservice.dto;
public record UserResponse(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String role,
        boolean active
) {
}

