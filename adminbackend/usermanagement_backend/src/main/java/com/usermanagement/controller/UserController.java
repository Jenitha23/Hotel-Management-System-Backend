package com.usermanagement.controller;

import com.usermanagement.entity.User;
import com.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal User user,
                                                @RequestParam(required = false) String newUsername,
                                                @RequestParam(required = false) String newEmail) {
        userService.updateProfile(user, newUsername, newEmail);
        return ResponseEntity.ok("Profile updated.");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminPage() {
        return ResponseEntity.ok("Admin access only.");
    }
}