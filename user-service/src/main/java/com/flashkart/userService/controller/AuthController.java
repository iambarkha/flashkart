package com.flashkart.userService.controller;

import com.flashkart.userService.dto.AuthResponse;
import com.flashkart.userService.dto.LoginRequest;
import com.flashkart.userService.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.flashkart.userService.service.AuthService;
import com.flashkart.userService.service.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
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
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.status(response.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.status(response.success() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String token) {
        String username = jwtService.extractUsername(token); // Extract username from token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Fetch UserDetails
        boolean isValid = jwtService.isTokenValid(token, userDetails); // Validate token with UserDetails

        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
