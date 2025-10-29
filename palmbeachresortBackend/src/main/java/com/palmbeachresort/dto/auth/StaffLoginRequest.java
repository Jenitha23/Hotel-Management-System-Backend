package com.palmbeachresort.dto.auth;

public class StaffLoginRequest extends LoginRequest {
    public StaffLoginRequest() {}
    public StaffLoginRequest(String email, String password) {
        super(email, password);
    }
}