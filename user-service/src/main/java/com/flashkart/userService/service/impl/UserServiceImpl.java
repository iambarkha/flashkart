package com.flashkart.userService.service.impl;

import com.flashkart.userService.dto.UserResponse;
import com.flashkart.userService.entity.User;
import com.flashkart.userService.mapper.UserMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.flashkart.userService.repository.UserRepository;
import com.flashkart.userService.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public UserResponse getUserByEmail(String email) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!currentUserEmail.equals(email)) {
            throw new AccessDeniedException("You are not authorized to access this user's data");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long userId, UserResponse userResponse) {
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

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateCurrentUser(UserResponse userResponse) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userResponse.firstName());
        user.setLastName(userResponse.lastName());
        user.setPhoneNumber(userResponse.phoneNumber());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }
}
