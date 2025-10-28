package com.hotelmanagement.scru167.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name="bookings")
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) private Customer customer;
    @ManyToOne(optional=false) private Room room;

    @Column(nullable=false) private LocalDate checkIn;
    @Column(nullable=false) private LocalDate checkOut; // exclusive

    @Column(nullable=false) private boolean checkedOut = false;
}
