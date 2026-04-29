package com.dance.me.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.auth.dto.AuthResponse;
import com.dance.me.auth.dto.ForgotPasswordRequest;
import com.dance.me.auth.dto.LoginRequest;
import com.dance.me.auth.dto.ResetPasswordRequest;
import com.dance.me.auth.entity.EmailVerificationToken;
import com.dance.me.auth.entity.PasswordResetToken;
import com.dance.me.auth.entity.RefreshToken;
import com.dance.me.auth.repository.EmailVerificationTokenRepository;
import com.dance.me.auth.repository.PasswordResetTokenRepository;
import com.dance.me.auth.repository.RefreshTokenRepository;
import com.dance.me.common.exception.AuthException;
import com.dance.me.common.exception.BadRequestException;
import com.dance.me.config.AppProperties;
import com.dance.me.user.dto.RegisterRequest;
import com.dance.me.user.entity.User;
import com.dance.me.user.mapper.UserMapper;
import com.dance.me.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailVerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final AppProperties appProperties;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailWithSchools(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!user.getActive()) {
            throw new AuthException("Account is disabled");
        }

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }

        User user = userRepository.save(User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build());

        sendVerificationEmail(user);

        return buildAuthResponse(user);
    }

    @Transactional
    public void verifyEmail(String tokenValue) {
        EmailVerificationToken token = verificationTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new AuthException("Invalid verification token"));

        if (token.getUsed()) {
            throw new AuthException("Verification token already used");
        }
        if (token.isExpired()) {
            throw new AuthException("Verification token has expired");
        }

        token.setUsed(true);

        User user = token.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public AuthResponse refresh(String tokenValue) {
        RefreshToken refreshToken = refreshTokenService.verify(tokenValue);

        User user = userRepository.findByEmailWithSchools(refreshToken.getUser().getEmail())
                .orElseThrow(() -> new AuthException("User not found"));

        return buildAuthResponse(user);
    }

    @Transactional
    public void logout(String tokenValue) {
        refreshTokenService.revoke(tokenValue);
    }

    // ── Forgot / Reset password ───────────────────────────────────────────────

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // Respuesta idéntica si el email existe o no — evita enumerar usuarios
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            // Invalida códigos anteriores del mismo usuario
            passwordResetTokenRepository.findAllByUserAndUsedFalse(user)
                    .forEach(t -> t.setUsed(true));

            String code = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));

            passwordResetTokenRepository.save(PasswordResetToken.builder()
                    .user(user)
                    .code(code)
                    .expiresAt(LocalDateTime.now().plusMinutes(15))
                    .build());

            emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), code);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = passwordResetTokenRepository
                .findByUserEmailAndCodeAndUsedFalse(request.getEmail(), request.getCode())
                .orElseThrow(() -> new AuthException("Código inválido o incorrecto"));

        if (token.isExpired()) {
            throw new AuthException("El código ha expirado. Solicita uno nuevo");
        }

        token.setUsed(true);

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Revocar todas las sesiones activas — fuerza re-login en todos los dispositivos
        refreshTokenRepository.findAllByUserAndRevokedFalse(user)
                .forEach(t -> t.setRevoked(true));
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private void sendVerificationEmail(User user) {
        String tokenValue = UUID.randomUUID().toString();

        verificationTokenRepository.save(EmailVerificationToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build());

        String verificationUrl = appProperties.getBaseUrl()
                + "/api/v1/auth/verify?token=" + tokenValue;

        // @Async — does not block the register response
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationUrl);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(userMapper.toResponse(user))
                .build();
    }
}
