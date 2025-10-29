package com.palmbeachresort.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "admins")
public class Admin extends User {

    public Admin() {}

    public Admin(String email, String password, String fullName) {
        super(email, password, fullName, "ADMIN");
    }
}