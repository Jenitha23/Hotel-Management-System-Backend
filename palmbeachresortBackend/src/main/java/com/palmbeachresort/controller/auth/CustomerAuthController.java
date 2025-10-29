package com.palmbeachresort.controller.auth;

import com.palmbeachresort.dto.auth.CustomerRegRequest;
import com.palmbeachresort.dto.auth.AuthResponse;
import com.palmbeachresort.dto.auth.CustomerLoginRequest;
import com.palmbeachresort.service.auth.CustomerAuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/auth")
public class CustomerAuthController {

    private final CustomerAuthService customerAuthService;

    public CustomerAuthController(CustomerAuthService customerAuthService) {
        this.customerAuthService = customerAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody CustomerRegRequest request) {
        AuthResponse response = customerAuthService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody CustomerLoginRequest request, HttpSession session) {
        AuthResponse response = customerAuthService.login(request, session);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = customerAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentCustomer(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(new AuthResponse("Access denied - not a customer", false));
        }

        String email = (String) session.getAttribute("email");
        String fullName = (String) session.getAttribute("fullName");

        return ResponseEntity.ok(new AuthResponse(
                "You are logged in as customer",
                true,
                userId,
                role,
                email,
                fullName
        ));
    }
}