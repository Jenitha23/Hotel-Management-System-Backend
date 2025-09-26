package com.hotelmanagement.bookingadmin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "room")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String roomNumber;

    @Column(nullable = false, length = 50)
    private String roomType; // e.g., DELUXE, STANDARD, SUITE
}
