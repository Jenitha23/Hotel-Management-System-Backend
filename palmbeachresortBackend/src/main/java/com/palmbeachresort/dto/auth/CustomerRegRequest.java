package com.palmbeachresort.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerRegRequest extends RegisterRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phone;

    public CustomerRegRequest() {}

    public CustomerRegRequest(String email, String password, String fullName, String phone) {
        super(email, password, fullName);
        this.phone = phone;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}