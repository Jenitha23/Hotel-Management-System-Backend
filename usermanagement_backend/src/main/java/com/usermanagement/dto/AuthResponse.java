package com.usermanagement.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    // Add a constructor with parameters
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // No-args constructor (required by some frameworks like JPA)
    public AuthResponse() {
    }
}