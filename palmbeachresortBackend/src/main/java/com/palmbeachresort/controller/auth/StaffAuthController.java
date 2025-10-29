package com.palmbeachresort.controller.auth;

import com.palmbeachresort.dto.auth.StaffRegRequest;
import com.palmbeachresort.dto.auth.AuthResponse;
import com.palmbeachresort.dto.auth.StaffLoginRequest;
import com.palmbeachresort.service.auth.StaffAuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/auth")
public class StaffAuthController {

    private final StaffAuthService staffAuthService;

    public StaffAuthController(StaffAuthService staffAuthService) {
        this.staffAuthService = staffAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody StaffRegRequest request) {
        AuthResponse response = staffAuthService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody StaffLoginRequest request, HttpSession session) {
        AuthResponse response = staffAuthService.login(request, session);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = staffAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentStaff(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }

        if (!"STAFF".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(new AuthResponse("Access denied - not a staff member", false));
        }

        String email = (String) session.getAttribute("email");
        String fullName = (String) session.getAttribute("fullName");

        return ResponseEntity.ok(new AuthResponse(
                "You are logged in as staff member",
                true,
                userId,
                role,
                email,
                fullName
        ));
    }
}