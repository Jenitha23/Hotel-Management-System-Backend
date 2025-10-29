package com.palmbeachresort.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends User {
    private String phone;

    public Customer() {}

    public Customer(String email, String password, String fullName, String phone) {
        super(email, password, fullName, "CUSTOMER");
        this.phone = phone;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}