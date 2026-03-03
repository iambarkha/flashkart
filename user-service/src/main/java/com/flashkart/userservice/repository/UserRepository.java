package com.flashkart.userservice.repository;

import com.flashkart.userservice.entity.Role;
import com.flashkart.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> , JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Modifying query: update without loading entity
    @Modifying
    @Transactional
    @Query("update User u set u.enabled = false where u.id = :id")
    int deactivateById(@Param("id") UUID id);

    // Pageable example (derived + pagination)
    Page<User> findByRole(Role role, Pageable pageable);
}
