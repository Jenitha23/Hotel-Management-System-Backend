package com.palmbeachresort.service.auth;

import com.palmbeachresort.dto.auth.*;
import com.palmbeachresort.entity.auth.Staff;
import com.palmbeachresort.repository.auth.StaffRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StaffAuthService implements BaseAuthService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffAuthService(StaffRepository staffRepository,
                            PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        StaffRegRequest regRequest = (StaffRegRequest) request;

        // Check if email exists
        if (staffRepository.existsByEmail(regRequest.getEmail())) {
            return new AuthResponse("Email already registered", false);
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(regRequest.getPassword());

        // Create staff
        Staff staff = new Staff(
                regRequest.getEmail(),
                hashedPassword,
                regRequest.getFullName()
        );

        Staff savedStaff = staffRepository.save(staff);

        return new AuthResponse(
                "Staff registered successfully",
                true,
                savedStaff.getId(),
                savedStaff.getRole(),
                savedStaff.getEmail(),
                savedStaff.getFullName()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpSession session) {
        StaffLoginRequest loginRequest = (StaffLoginRequest) request;

        Staff staff = staffRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (staff == null || !passwordEncoder.matches(loginRequest.getPassword(), staff.getPassword())) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store user details in session
        session.setAttribute("userId", staff.getId());
        session.setAttribute("role", staff.getRole());
        session.setAttribute("email", staff.getEmail());
        session.setAttribute("fullName", staff.getFullName());

        return new AuthResponse(
                "Staff login successful",
                true,
                staff.getId(),
                staff.getRole(),
                staff.getEmail(),
                staff.getFullName()
        );
    }

    @Override
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logged out successfully", true);
    }
}