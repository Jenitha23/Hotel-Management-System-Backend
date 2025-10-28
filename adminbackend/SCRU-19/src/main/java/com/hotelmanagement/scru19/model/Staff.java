package com.hotelmanagement.scru19.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter
public class Staff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String email;
}