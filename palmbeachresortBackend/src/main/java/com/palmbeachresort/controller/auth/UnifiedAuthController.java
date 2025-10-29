package com.palmbeachresort.controller.auth;

import com.palmbeachresort.dto.auth.UnifiedLoginRequest;
import com.palmbeachresort.dto.auth.AuthResponse;
import com.palmbeachresort.service.auth.UnifiedAuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UnifiedAuthController {

    private final UnifiedAuthService unifiedAuthService;

    public UnifiedAuthController(UnifiedAuthService unifiedAuthService) {
        this.unifiedAuthService = unifiedAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> unifiedLogin(@Valid @RequestBody UnifiedLoginRequest request, HttpSession session) {
        AuthResponse response = unifiedAuthService.unifiedLogin(request, session);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> unifiedLogout(HttpSession session) {
        AuthResponse response = unifiedAuthService.unifiedLogout(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(HttpSession session) {
        AuthResponse response = unifiedAuthService.getCurrentUser(session);
        return ResponseEntity.ok(response);
    }
}