package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

public  record AuthResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        UserResponse user,
        String message,
        boolean success
) {
    @Builder
    public AuthResponse {
    }
}

