package com.flashkart.userService.dto;

public  record AuthResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        UserResponse user,
        String message,
        boolean success
) {
}

