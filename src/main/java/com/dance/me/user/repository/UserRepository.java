package com.dance.me.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dance.me.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
           "LEFT JOIN FETCH u.schools us " +
           "LEFT JOIN FETCH us.school " +
           "WHERE u.email = :email")
    Optional<User> findByEmailWithSchools(String email);

    boolean existsByEmail(String email);
}