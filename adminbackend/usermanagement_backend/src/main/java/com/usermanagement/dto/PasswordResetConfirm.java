package com.usermanagement.dto;

import lombok.Data;

@Data
public class PasswordResetConfirm {
    private String token;
    private String newPassword;
}