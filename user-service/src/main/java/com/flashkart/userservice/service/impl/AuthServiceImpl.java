package com.flashkart.userservice.service.impl;

import com.flashkart.userservice.dto.request.LoginRequest;
import com.flashkart.userservice.dto.request.RegisterRequest;
import com.flashkart.userservice.dto.response.AuthResponse;
import com.flashkart.userservice.entity.Role;
import com.flashkart.userservice.entity.User;
import com.flashkart.userservice.exception.InvalidCredentialsException;
import com.flashkart.userservice.exception.UserAlreadyExistsException;
import com.flashkart.userservice.mapper.UserMapper;
import com.flashkart.userservice.repository.UserRepository;
import com.flashkart.userservice.service.AuthService;
import com.flashkart.userservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;



    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(("Email already registered: " + request.email()));
        }
        User user = userMapper.toUser(request);
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return AuthResponse.of(
                            jwtService.generateToken(savedUser),
                            jwtService.getExpirationMs(),
                            userMapper.toUserResponse(savedUser)
        );

    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return AuthResponse.of(
                jwtService.generateToken(user),
                jwtService.getExpirationMs(),
                userMapper.toUserResponse(user)
        );
    }

}
