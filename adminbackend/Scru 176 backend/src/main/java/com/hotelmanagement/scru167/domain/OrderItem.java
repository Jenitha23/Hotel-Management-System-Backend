package com.hotelmanagement.scru167.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false) private OrderType type;

    @Column(nullable=false) private String description;
    @Column(nullable=false) private int quantity;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal unitPrice;

    @Column(nullable=false) private LocalDateTime orderedAt = LocalDateTime.now();
}
