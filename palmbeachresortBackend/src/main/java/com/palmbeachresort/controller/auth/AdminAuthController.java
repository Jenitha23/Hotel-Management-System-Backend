package com.palmbeachresort.controller.auth;

import com.palmbeachresort.dto.auth.AdminRegRequest;
import com.palmbeachresort.dto.auth.AuthResponse;
import com.palmbeachresort.dto.auth.AdminLoginRequest;
import com.palmbeachresort.service.auth.AdminAuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    // Register Admin
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AdminRegRequest request) {
        AuthResponse response = adminAuthService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Admin Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AdminLoginRequest request, HttpSession session) {
        AuthResponse response = adminAuthService.login(request, session);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    // Admin Logout
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = adminAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    // Get Current Admin Info
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentAdmin(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }

        // Verify the user has ADMIN role
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(new AuthResponse("Access denied - not an admin", false));
        }

        String email = (String) session.getAttribute("email");
        String fullName = (String) session.getAttribute("fullName");

        return ResponseEntity.ok(new AuthResponse(
                "You are logged in as admin",
                true,
                userId,
                role,
                email,
                fullName
        ));
    }
}