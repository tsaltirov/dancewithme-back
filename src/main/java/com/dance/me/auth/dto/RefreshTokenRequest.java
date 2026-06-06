package com.dance.me.auth.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    // Optional — refresh token can also arrive via cookie
    private String refreshToken;
}
