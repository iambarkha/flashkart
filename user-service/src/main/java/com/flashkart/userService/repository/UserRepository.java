package com.flashkart.userService.repository;

import com.flashkart.userService.entity.User;
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

@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    // JPQL: case-insensitive partial name search among active users
    @Query("select u from User u where lower(concat(u.firstName, ' ', u.lastName)) like lower(concat('%', :name, '%')) and u.active = true")
    List<User> searchActiveByName(@Param("name") String name);

    // Native SQL: when you need DB-specific functions or exact column names
    @Query(value = "SELECT * FROM users u WHERE u.phone_number = :phone", nativeQuery = true)
    Optional<User> findByPhoneNative(@Param("phone") String phone);

    // Modifying query: update without loading entity
    @Modifying
    @Transactional
    @Query("update User u set u.active = false where u.id = :id")
    int deactivateById(@Param("id") Long id);

    /*// DTO constructor projection (requires matching constructor in dto.UserSummary)
    @Query("select new dto.UserSummary(u.id, concat(u.firstName, ' ', u.lastName)) from User u where u.active = true")
    List<dto.UserSummary> findActiveUserSummaries();
*/
    // Pageable example (derived + pagination)
    Page<User> findByRole(String role, Pageable pageable);
}
