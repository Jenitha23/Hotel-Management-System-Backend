package org.example.booking_management_backend1.service;




import org.example.booking_management_backend1.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    List<Room> getAvailableRooms();
    List<Room> getAvailableRoomsForDates(LocalDate checkInDate, LocalDate checkOutDate);
    Room getRoomById(Long id);
}
