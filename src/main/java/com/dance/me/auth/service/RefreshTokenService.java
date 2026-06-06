package com.dance.me.auth.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.auth.entity.RefreshToken;
import com.dance.me.auth.repository.RefreshTokenRepository;
import com.dance.me.common.exception.AuthException;
import com.dance.me.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public RefreshToken create(User user) {
        // Revoke all active tokens for this user (rotation)
        refreshTokenRepository.findAllByUserAndRevokedFalse(user)
                .forEach(t -> t.setRevoked(true));

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiration(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        return refreshTokenRepository.save(token);
    }

    public RefreshToken verify(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new AuthException("Invalid refresh token"));

        if (token.getRevoked()) {
            throw new AuthException("Refresh token has been revoked");
        }
        if (token.isExpired()) {
            throw new AuthException("Refresh token has expired");
        }

        return token;
    }

    @Transactional
    public void revoke(String tokenValue) {
        refreshTokenRepository.findByToken(tokenValue)
                .ifPresent(t -> t.setRevoked(true));
    }
}
