package service.impl;

import config.JwtService;
import dto.AuthResponse;
import dto.LoginRequest;
import dto.RegisterRequest;
import dto.UserResponse;
import entity.User;
import mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import service.AuthService;

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
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.email())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email already registered")
                    .build();
        }

        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .user(userMapper.toUserResponse(savedUser))
                .success(true)
                .message("User registered successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid email or password")
                    .build();
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .user(userMapper.toUserResponse(user))
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24 hours
                .success(true)
                .message("Login successful")
                .build();
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
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
