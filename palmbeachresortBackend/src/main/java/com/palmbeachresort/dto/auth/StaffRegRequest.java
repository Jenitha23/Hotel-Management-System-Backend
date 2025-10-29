package com.palmbeachresort.dto.auth;

public class StaffRegRequest extends RegisterRequest {
    public StaffRegRequest() {}
    public StaffRegRequest(String email, String password, String fullName) {
        super(email, password, fullName);
    }
}