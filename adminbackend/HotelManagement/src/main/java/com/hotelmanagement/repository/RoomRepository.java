package com.hotelmanagement.repository;

import com.hotelmanagement.entity.Room;
import com.hotelmanagement.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for Room.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByAvailable(boolean available);

    List<Room> findByType(RoomType type);

    List<Room> findByAvailableAndType(boolean available, RoomType type);
}
