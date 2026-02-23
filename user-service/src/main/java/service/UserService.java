package service;

import dto.UserResponse;

public interface UserService {

    UserResponse getUserById(Long userId);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long userId, UserResponse userResponse);

    void deleteUser(Long userId);

    boolean existsByEmail(String email);
}


