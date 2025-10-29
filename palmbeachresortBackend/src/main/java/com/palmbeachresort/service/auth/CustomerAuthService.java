package com.palmbeachresort.service.auth;

import com.palmbeachresort.dto.auth.*;
import com.palmbeachresort.entity.auth.Customer;
import com.palmbeachresort.repository.auth.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerAuthService implements BaseAuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerAuthService(CustomerRepository customerRepository,
                               PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        CustomerRegRequest regRequest = (CustomerRegRequest) request;

        // Check if email exists
        if (customerRepository.existsByEmail(regRequest.getEmail())) {
            return new AuthResponse("Email already registered", false);
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(regRequest.getPassword());

        // Create customer
        Customer customer = new Customer(
                regRequest.getEmail(),
                hashedPassword,
                regRequest.getFullName(),
                regRequest.getPhone()
        );

        Customer savedCustomer = customerRepository.save(customer);

        return new AuthResponse(
                "Customer registered successfully! Welcome to Palm Beach Resort.",
                true,
                savedCustomer.getId(),
                savedCustomer.getRole(),
                savedCustomer.getEmail(),
                savedCustomer.getFullName()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpSession session) {
        CustomerLoginRequest loginRequest = (CustomerLoginRequest) request;

        Customer customer = customerRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (customer == null || !passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store user details in session
        session.setAttribute("userId", customer.getId());
        session.setAttribute("role", customer.getRole());
        session.setAttribute("email", customer.getEmail());
        session.setAttribute("fullName", customer.getFullName());

        return new AuthResponse(
                "Welcome back to Palm Beach Resort!",
                true,
                customer.getId(),
                customer.getRole(),
                customer.getEmail(),
                customer.getFullName()
        );
    }

    @Override
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logged out successfully", true);
    }
}