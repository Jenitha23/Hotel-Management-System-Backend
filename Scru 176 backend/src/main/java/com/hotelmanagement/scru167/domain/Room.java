package com.hotelmanagement.scru167.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name="rooms")
public class Room {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String roomNumber;

    @Column(nullable=false)
    private String type; // e.g. STANDARD, DELUXE

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal dailyRate;
}
