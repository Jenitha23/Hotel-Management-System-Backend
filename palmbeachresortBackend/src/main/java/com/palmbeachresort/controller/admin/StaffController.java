// [file name]: StaffController.java
package com.palmbeachresort.controller.admin;

import com.palmbeachresort.entity.auth.Staff;
import com.palmbeachresort.repository.auth.StaffRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffRepository staffRepository;

    public StaffController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    // Get all staff members
    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        List<Staff> staffList = staffRepository.findAll();

        List<StaffResponse> response = staffList.stream()
                .map(staff -> new StaffResponse(
                        staff.getId(),
                        staff.getFullName(),
                        staff.getEmail(),
                        staff.getRole(),
                        staff.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Get staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long id) {
        return staffRepository.findById(id)
                .map(staff -> ResponseEntity.ok(new StaffResponse(
                        staff.getId(),
                        staff.getFullName(),
                        staff.getEmail(),
                        staff.getRole(),
                        staff.getCreatedAt()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete staff account
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        try {
            if (staffRepository.existsById(id)) {
                staffRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting staff");
        }
    }

    // Staff response DTO
    public static class StaffResponse {
        private Long id;
        private String fullName;
        private String email;
        private String role;
        private String createdAt;
        private String status = "Active";

        public StaffResponse(Long id, String fullName, String email, String role, java.time.Instant createdAt) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.createdAt = createdAt != null ?
                    java.time.format.DateTimeFormatter.ISO_INSTANT.format(createdAt) :
                    null;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}