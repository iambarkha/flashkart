package com.flashkart.userService.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import com.flashkart.userService.entity.User;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(User user);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
