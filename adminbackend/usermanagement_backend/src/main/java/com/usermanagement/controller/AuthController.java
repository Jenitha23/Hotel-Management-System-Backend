package com.usermanagement.controller;
import com.usermanagement.entity.User;
import com.usermanagement.dto.*;
import com.usermanagement.service.AuthService;
import com.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);
        authService.sendVerificationEmail(user);
        return ResponseEntity.ok("User registered. Check email for verification.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok("Password reset link sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> confirmPasswordReset(@RequestBody PasswordResetConfirm confirm) {
        authService.confirmPasswordReset(confirm);
        return ResponseEntity.ok("Password reset successfully.");
    }
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal User user,
                                                @RequestParam(required = false) String newUsername,
                                                @RequestParam(required = false) String newEmail) {
        userService.updateProfile(user, newUsername, newEmail);
        return ResponseEntity.ok("Profile updated.");
    }
}