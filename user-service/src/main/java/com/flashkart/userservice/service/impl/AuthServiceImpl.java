package com.flashkart.userservice.service.impl;

import com.flashkart.userservice.dto.request.LoginRequest;
import com.flashkart.userservice.dto.request.RegisterRequest;
import com.flashkart.userservice.dto.response.ApiResponse;
import com.flashkart.userservice.dto.response.AuthResponse;
import com.flashkart.userservice.dto.response.UserResponse;
import com.flashkart.userservice.entity.Role;
import com.flashkart.userservice.exception.InvalidCredentialsException;
import com.flashkart.userservice.exception.ResourceNotFoundException;
import com.flashkart.userservice.exception.UserAlreadyExistsException;
import com.flashkart.userservice.service.JwtService;
import com.flashkart.userservice.entity.User;
import com.flashkart.userservice.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.flashkart.userservice.repository.UserRepository;
import com.flashkart.userservice.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ApiResponse<AuthResponse> register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.email())) {
            throw new UserAlreadyExistsException(("Email already registered: " + registerRequest.email()));
        }
        User user = userMapper.toUser(registerRequest);
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        User savedUser = userRepository.save(user);

       AuthResponse response =  new AuthResponse( jwtService.generateToken(savedUser),
               "Bearer", 86400L, userMapper.toUserResponse(savedUser));

        return ApiResponse.success("User registered successfully", response);
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        AuthResponse authResponse = new AuthResponse(
                jwtService.generateToken(user),
                "Bearer",
                86400L,
                userMapper.toUserResponse(user)
        );
        return ApiResponse.success("User registered successfully", authResponse);

    }

    @Override
    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUserProfile(Long userId, UserResponse userResponse) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userResponse.firstName());
        user.setLastName(userResponse.lastName());
        user.setPhoneNumber(userResponse.phoneNumber());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userid: " + userId));
        userRepository.delete(user);
    }
}
