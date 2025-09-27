package org.example.booking_management_backend1.repository;


import org.example.booking_management_backend1.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailableTrue();

    @Query("SELECT r FROM Room r WHERE r.available = true AND r.id NOT IN " +
            "(SELECT b.room.id FROM Booking b WHERE b.status = 'CONFIRMED' AND " +
            "((b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)))")
    List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);
}
