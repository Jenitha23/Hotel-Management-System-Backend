package com.palmbeachresort.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=== PALM BEACH RESORT PASSWORD HASH GENERATOR ===");
        System.out.println();

        // Admin passwords
        String adminPassword1 = encoder.encode("Admin123!");
        String adminPassword2 = encoder.encode("admin123");

        System.out.println("ADMIN PASSWORDS:");
        System.out.println("Email: admin@resort.com");
        System.out.println("Password: Admin123!");
        System.out.println("Hash: " + adminPassword1);
        System.out.println();
        System.out.println("Email: admin@resort.com");
        System.out.println("Password: admin123");
        System.out.println("Hash: " + adminPassword2);
        System.out.println();

        // Staff passwords
        String staffPassword1 = encoder.encode("Staff123!");
        String staffPassword2 = encoder.encode("staff123");

        System.out.println("STAFF PASSWORDS:");
        System.out.println("Email: staff@resort.com");
        System.out.println("Password: Staff123!");
        System.out.println("Hash: " + staffPassword1);
        System.out.println();
        System.out.println("Email: staff@resort.com");
        System.out.println("Password: staff123");
        System.out.println("Hash: " + staffPassword2);
        System.out.println();

        // Customer passwords
        String customerPassword1 = encoder.encode("Customer123!");
        String customerPassword2 = encoder.encode("customer123");

        System.out.println("CUSTOMER PASSWORDS:");
        System.out.println("Email: customer@example.com");
        System.out.println("Password: Customer123!");
        System.out.println("Hash: " + customerPassword1);
        System.out.println();
        System.out.println("Email: customer@example.com");
        System.out.println("Password: customer123");
        System.out.println("Hash: " + customerPassword2);
        System.out.println();

        // Test passwords for development
        String testPassword = encoder.encode("test123");
        String simplePassword = encoder.encode("password");

        System.out.println("TEST PASSWORDS:");
        System.out.println("Password: test123");
        System.out.println("Hash: " + testPassword);
        System.out.println();
        System.out.println("Password: password");
        System.out.println("Hash: " + simplePassword);
        System.out.println();

        System.out.println("=== SQL INSERT STATEMENTS ===");
        System.out.println();

        // SQL for admin
        System.out.println("-- For Admin (use one of these):");
        System.out.println("INSERT INTO admins (email, password, full_name, role, created_at) VALUES ('admin@resort.com', '" + adminPassword1 + "', 'System Administrator', 'ADMIN', NOW());");
        System.out.println("-- OR:");
        System.out.println("INSERT INTO admins (email, password, full_name, role, created_at) VALUES ('admin@resort.com', '" + adminPassword2 + "', 'System Administrator', 'ADMIN', NOW());");
        System.out.println();

        // SQL for staff
        System.out.println("-- For Staff:");
        System.out.println("INSERT INTO staff (email, password, full_name, role, created_at) VALUES ('staff@resort.com', '" + staffPassword1 + "', 'John Staff', 'STAFF', NOW());");
        System.out.println();

        // SQL for customer
        System.out.println("-- For Customer:");
        System.out.println("INSERT INTO customers (email, password, full_name, phone, role, created_at) VALUES ('customer@example.com', '" + customerPassword1 + "', 'John Customer', '+1234567890', 'CUSTOMER', NOW());");
        System.out.println();

        System.out.println("=== TEST CREDENTIALS SUMMARY ===");
        System.out.println();
        System.out.println("ADMIN:");
        System.out.println("  Email: admin@resort.com");
        System.out.println("  Password: Admin123! OR admin123");
        System.out.println();
        System.out.println("STAFF:");
        System.out.println("  Email: staff@resort.com");
        System.out.println("  Password: Staff123! OR staff123");
        System.out.println();
        System.out.println("CUSTOMER:");
        System.out.println("  Email: customer@example.com");
        System.out.println("  Password: Customer123! OR customer123");
        System.out.println();

        // Verify some passwords
        System.out.println("=== PASSWORD VERIFICATION ===");
        System.out.println("Verify 'Admin123!': " + encoder.matches("Admin123!", adminPassword1));
        System.out.println("Verify 'admin123': " + encoder.matches("admin123", adminPassword2));
        System.out.println("Verify 'Staff123!': " + encoder.matches("Staff123!", staffPassword1));
        System.out.println("Verify 'staff123': " + encoder.matches("staff123", staffPassword2));
    }
}