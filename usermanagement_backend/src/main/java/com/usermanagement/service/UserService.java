package com.usermanagement.service;

import com.usermanagement.dto.RegisterRequest;
import com.usermanagement.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    void updateProfile(User user, String newUsername, String newEmail);
}