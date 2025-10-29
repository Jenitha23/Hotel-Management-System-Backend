package com.palmbeachresort.dto.auth;

public class CustomerLoginRequest extends LoginRequest {
    public CustomerLoginRequest() {}
    public CustomerLoginRequest(String email, String password) {
        super(email, password);
    }
}