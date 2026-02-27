package com.flashkart.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public  class AuthResponse{
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private UserResponse user;
    private String message;
    private boolean success;
}



