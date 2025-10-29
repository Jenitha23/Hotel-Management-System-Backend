package com.palmbeachresort.service.auth;

import com.palmbeachresort.dto.auth.*;
import com.palmbeachresort.entity.auth.Admin;
import com.palmbeachresort.repository.auth.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminAuthService implements BaseAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthService(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        AdminRegRequest regRequest = (AdminRegRequest) request;

        // Only allow one admin
        if (adminRepository.count() > 5) {
            return new AuthResponse("Only one admin is allowed", false);
        }

        // Check if email exists
        if (adminRepository.existsByEmail(regRequest.getEmail())) {
            return new AuthResponse("Email already registered", false);
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(regRequest.getPassword());

        // Create admin
        Admin admin = new Admin(
                regRequest.getEmail(),
                hashedPassword,
                regRequest.getFullName()
        );

        Admin savedAdmin = adminRepository.save(admin);

        return new AuthResponse(
                "Admin registered successfully",
                true,
                savedAdmin.getId(),
                savedAdmin.getRole(),
                savedAdmin.getEmail(),
                savedAdmin.getFullName()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpSession session) {
        AdminLoginRequest loginRequest = (AdminLoginRequest) request;

        Admin admin = adminRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (admin == null || !passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store user details in session
        session.setAttribute("userId", admin.getId());
        session.setAttribute("role", admin.getRole());
        session.setAttribute("email", admin.getEmail());
        session.setAttribute("fullName", admin.getFullName());

        return new AuthResponse(
                "Admin login successful",
                true,
                admin.getId(),
                admin.getRole(),
                admin.getEmail(),
                admin.getFullName()
        );
    }

    @Override
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logged out successfully", true);
    }
}