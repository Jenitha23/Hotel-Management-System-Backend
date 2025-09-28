package com.hotelmanagement.bookingadmin.service;

import com.hotelmanagement.bookingadmin.domain.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Objects;

public class BookingSpecifications {

    public static Specification<Booking> hasStatus(BookingStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Booking> hasRoomType(String roomType) {
        return (root, query, cb) -> {
            if (roomType == null || roomType.isBlank()) return cb.conjunction();
            Join<Booking, Room> roomJoin = root.join("room", JoinType.INNER);
            return cb.equal(cb.upper(roomJoin.get("roomType")), roomType.toUpperCase());
        };
    }

    /** Filter bookings that intersect a [from, to] date window (by check-in/out). */
    public static Specification<Booking> intersectsDateRange(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (Objects.isNull(from) && Objects.isNull(to)) return cb.conjunction();
            Path<LocalDate> checkIn = root.get("checkInDate");
            Path<LocalDate> checkOut = root.get("checkOutDate");

            if (from != null && to != null) {

                return cb.and(
                        cb.lessThanOrEqualTo(checkIn, to),
                        cb.greaterThanOrEqualTo(checkOut, from)
                );
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(checkOut, from);
            } else {
                return cb.lessThanOrEqualTo(checkIn, to);
            }
        };
    }
}
