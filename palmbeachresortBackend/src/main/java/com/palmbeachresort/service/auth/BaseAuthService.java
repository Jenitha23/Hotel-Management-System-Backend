package com.palmbeachresort.service.auth;

import com.palmbeachresort.dto.auth.AuthResponse;
import com.palmbeachresort.dto.auth.LoginRequest;
import com.palmbeachresort.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpSession;

public interface BaseAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request, HttpSession session);
    AuthResponse logout(HttpSession session);
}