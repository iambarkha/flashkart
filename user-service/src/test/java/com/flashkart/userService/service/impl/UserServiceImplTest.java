package com.flashkart.userService.service.impl;

import com.flashkart.userService.entity.User;
import com.flashkart.userService.entity.Role;
import com.flashkart.userService.repository.UserRepository;
import com.flashkart.userService.dto.UserResponse;
import com.flashkart.userService.exception.ResourceNotFoundException;
import com.flashkart.userService.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class UserServiceImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void getUserById_returnsUserResponse_whenUserExists() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("password")
                .phoneNumber("1234567890")
                .role(Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);

        UserResponse response = userService.getUserById(user.getUserId());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("john@example.com", response.email());
    }

    @Test
    void getUserById_throwsResourceNotFoundException_whenUserDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
    }
}
