package com.usermanagement.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}