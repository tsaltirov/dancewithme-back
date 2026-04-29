package com.dance.me.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dance.me.auth.entity.PasswordResetToken;
import com.dance.me.user.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByUserEmailAndCodeAndUsedFalse(String email, String code);

    List<PasswordResetToken> findAllByUserAndUsedFalse(User user);
}
