package com.palmbeachresort.dto.auth;

public class AuthResponse {
    private String message;
    private boolean success;
    private Long userId;
    private String role;
    private String email;
    private String fullName;

    public AuthResponse() {}

    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public AuthResponse(String message, boolean success, Long userId, String role, String email, String fullName) {
        this.message = message;
        this.success = success;
        this.userId = userId;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}