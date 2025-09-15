package com.hotelmanagement.scrum11.rooms;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "rooms")
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private RoomType type;

    @NotNull @DecimalMin("0.0") @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull @Column(nullable = false)
    private Boolean available;

    @DecimalMin("0.0") @DecimalMax("5.0")
    private Double rating;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp private LocalDateTime updatedAt;
}
