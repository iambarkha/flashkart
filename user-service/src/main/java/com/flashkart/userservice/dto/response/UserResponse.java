package com.flashkart.userservice.dto.response;

import com.flashkart.userservice.entity.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Role role,
        boolean enabled
) {
}

