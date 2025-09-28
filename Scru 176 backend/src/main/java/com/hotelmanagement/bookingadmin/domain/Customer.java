package com.hotelmanagement.bookingadmin.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "customer")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 150, unique = true)
    private String email;
}
