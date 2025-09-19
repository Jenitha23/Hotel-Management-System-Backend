package com.usermanagement.service;

import com.usermanagement.dto.AuthResponse;
import com.usermanagement.dto.LoginRequest;
import com.usermanagement.dto.PasswordResetConfirm;
import com.usermanagement.dto.PasswordResetRequest;
import com.usermanagement.entity.User;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void sendVerificationEmail(User user);
    void verifyEmail(String token);
    void requestPasswordReset(PasswordResetRequest request);
    void confirmPasswordReset(PasswordResetConfirm confirm);
}