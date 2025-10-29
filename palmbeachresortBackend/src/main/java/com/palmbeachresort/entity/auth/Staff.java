package com.palmbeachresort.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff extends User {

    public Staff() {}

    public Staff(String email, String password, String fullName) {
        super(email, password, fullName, "STAFF");
    }
}