package com.hotelmanagement.scru19.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="staff_id")
    private Staff staff;

    @Column(nullable=false) private String title;
    private String location;
    @Enumerated(EnumType.STRING) private Status status = Status.PLANNED;
    @Column(nullable=false) private LocalDateTime startAt;
    @Column(nullable=false) private LocalDateTime endAt;
    private String notes;
    private String recurrenceRule; // optional

    public enum Status { PLANNED, CONFIRMED, CANCELLED }
}
