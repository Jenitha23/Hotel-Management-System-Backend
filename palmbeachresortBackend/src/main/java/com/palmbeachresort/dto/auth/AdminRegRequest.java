package com.palmbeachresort.dto.auth;

public class AdminRegRequest extends RegisterRequest {
    public AdminRegRequest() {}
    public AdminRegRequest(String email, String password, String fullName) {
        super(email, password, fullName);
    }
}