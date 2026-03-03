package com.flashkart.userservice.controller;


import com.flashkart.userservice.dto.response.ApiResponse;
import com.flashkart.userservice.dto.response.AuthResponse;
import com.flashkart.userservice.dto.request.LoginRequest;
import com.flashkart.userservice.dto.request.RegisterRequest;
import com.flashkart.userservice.service.AuthService;
import com.flashkart.userservice.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(AuthService authService, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account with email and password")


    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest) {

        AuthResponse authResponse = authService.register(registerRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", authResponse));
    }
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")

    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity
                .ok(ApiResponse.success("Login successful", authResponse));
    }


    @GetMapping("/validate")
    @Operation(summary = "Validate JWT token", description = "Check if a JWT token is valid")

    public ResponseEntity<String> validate(@RequestParam String token) {
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
