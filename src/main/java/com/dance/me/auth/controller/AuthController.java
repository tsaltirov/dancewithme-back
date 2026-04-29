package com.dance.me.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.auth.dto.AuthResponse;
import com.dance.me.auth.dto.ForgotPasswordRequest;
import com.dance.me.auth.dto.LoginRequest;
import com.dance.me.auth.dto.RefreshTokenRequest;
import com.dance.me.auth.dto.ResetPasswordRequest;
import com.dance.me.auth.service.AuthService;
import com.dance.me.common.dto.ApiResponse;
import com.dance.me.user.dto.RegisterRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse auth = authService.login(request);
        return ResponseEntity.ok()
                .headers(buildAuthCookies(auth.getAccessToken(), auth.getRefreshToken()))
                .body(ApiResponse.ok("Login successful", auth));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse auth = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(buildAuthCookies(auth.getAccessToken(), auth.getRefreshToken()))
                .body(ApiResponse.ok("User registered successfully", auth));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @CookieValue(name = "refresh_token", required = false) String cookieToken,
            @RequestBody(required = false) RefreshTokenRequest body) {

        String token = cookieToken != null ? cookieToken
                : (body != null ? body.getRefreshToken() : null);

        AuthResponse auth = authService.refresh(token);
        return ResponseEntity.ok()
                .headers(buildAuthCookies(auth.getAccessToken(), auth.getRefreshToken()))
                .body(ApiResponse.ok("Token refreshed", auth));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.ok("Email verified successfully", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        // Respuesta siempre igual — no revela si el email existe o no
        return ResponseEntity.ok(ApiResponse.ok(
                "Si el correo está registrado, recibirás un código en breve", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.ok("Contraseña restablecida correctamente", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refresh_token", required = false) String cookieToken,
            @RequestBody(required = false) RefreshTokenRequest body) {

        String token = cookieToken != null ? cookieToken
                : (body != null ? body.getRefreshToken() : null);

        authService.logout(token);
        return ResponseEntity.ok()
                .headers(clearAuthCookies())
                .body(ApiResponse.ok("Logged out", null));
    }

    // --- Cookie helpers ---

    private HttpHeaders buildAuthCookies(String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie(accessToken).toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie(refreshToken).toString());
        return headers;
    }

    private HttpHeaders clearAuthCookies() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, ResponseCookie.from("access_token", "")
                .httpOnly(true).path("/").maxAge(0).build().toString());
        headers.add(HttpHeaders.SET_COOKIE, ResponseCookie.from("refresh_token", "")
                .httpOnly(true).path("/api/v1/auth").maxAge(0).build().toString());
        return headers;
    }

    private ResponseCookie accessCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(15 * 60)          // 15 min
                .sameSite("Lax")
                .build();
    }

    private ResponseCookie refreshCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .path("/api/v1/auth")     // scoped — only sent to /auth endpoints
                .maxAge(30L * 24 * 60 * 60) // 30 days
                .sameSite("Lax")
                .build();
    }
}
