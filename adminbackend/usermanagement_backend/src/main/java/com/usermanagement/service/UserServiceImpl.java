package com.usermanagement.service;

import com.usermanagement.dto.RegisterRequest;
import com.usermanagement.entity.User;
import com.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent() ||
                userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : com.usermanagement.entity.Role.CUSTOMER);
        user.setEnabled(false);
        return userRepository.save(user);
    }

    @Override
    public void updateProfile(User user, String newUsername, String newEmail) {
        if (newUsername != null) user.setUsername(newUsername);
        if (newEmail != null) user.setEmail(newEmail);
        userRepository.save(user);
    }
}