package com.palmbeachresort.service.auth;

import com.palmbeachresort.dto.auth.UnifiedLoginRequest;
import com.palmbeachresort.dto.auth.AuthResponse;
import com.palmbeachresort.entity.auth.Admin;
import com.palmbeachresort.entity.auth.Customer;
import com.palmbeachresort.entity.auth.Staff;
import com.palmbeachresort.repository.auth.AdminRepository;
import com.palmbeachresort.repository.auth.CustomerRepository;
import com.palmbeachresort.repository.auth.StaffRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UnifiedAuthService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public UnifiedAuthService(CustomerRepository customerRepository,
                              StaffRepository staffRepository,
                              AdminRepository adminRepository,
                              PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse unifiedLogin(UnifiedLoginRequest request, HttpSession session) {
        // Check customers first
        Customer customer = customerRepository.findByEmail(request.getEmail()).orElse(null);
        if (customer != null && passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            session.setAttribute("userId", customer.getId());
            session.setAttribute("role", customer.getRole());
            session.setAttribute("email", customer.getEmail());
            session.setAttribute("fullName", customer.getFullName());
            return new AuthResponse("Welcome back to Palm Beach Resort!", true,
                    customer.getId(), customer.getRole(), customer.getEmail(), customer.getFullName());
        }

        // Check staff
        Staff staff = staffRepository.findByEmail(request.getEmail()).orElse(null);
        if (staff != null && passwordEncoder.matches(request.getPassword(), staff.getPassword())) {
            session.setAttribute("userId", staff.getId());
            session.setAttribute("role", staff.getRole());
            session.setAttribute("email", staff.getEmail());
            session.setAttribute("fullName", staff.getFullName());
            return new AuthResponse("Staff login successful", true,
                    staff.getId(), staff.getRole(), staff.getEmail(), staff.getFullName());
        }

        // Check admins
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElse(null);
        if (admin != null && passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            session.setAttribute("userId", admin.getId());
            session.setAttribute("role", admin.getRole());
            session.setAttribute("email", admin.getEmail());
            session.setAttribute("fullName", admin.getFullName());
            return new AuthResponse("Admin login successful", true,
                    admin.getId(), admin.getRole(), admin.getEmail(), admin.getFullName());
        }

        return new AuthResponse("Invalid email or password", false);
    }

    public AuthResponse unifiedLogout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logout successful", true);
    }

    public AuthResponse getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        String email = (String) session.getAttribute("email");
        String fullName = (String) session.getAttribute("fullName");

        if (userId != null && role != null) {
            return new AuthResponse("User is logged in", true, userId, role, email, fullName);
        }

        return new AuthResponse("No user logged in", false);
    }
}