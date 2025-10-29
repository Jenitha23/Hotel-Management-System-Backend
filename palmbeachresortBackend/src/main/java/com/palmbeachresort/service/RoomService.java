package com.palmbeachresort.service;

import com.palmbeachresort.dto.RoomRequest;
import com.palmbeachresort.dto.RoomResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RoomService {

    // Admin operations - Auto-save to MySQL
    RoomResponse createRoom(RoomRequest roomRequest);
    RoomResponse updateRoom(Long id, RoomRequest roomRequest);
    RoomResponse updateRoomPartial(Long id, Map<String, Object> updates);
    void deleteRoom(Long id);
    List<RoomResponse> getAllRooms();

    // Customer operations
    List<RoomResponse> getAvailableRooms();
    List<RoomResponse> getRoomsByType(String roomType);
    List<RoomResponse> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<RoomResponse> getRoomsByCapacity(Integer capacity);
    List<RoomResponse> searchRooms(String roomType, BigDecimal minPrice, BigDecimal maxPrice, Integer capacity);
    List<String> getAvailableRoomTypes();
    RoomResponse getRoomById(Long id);

    // Utility methods
    boolean existsByRoomNumber(String roomNumber);
    RoomResponse getRoomByRoomNumber(String roomNumber);

    // Sorting
    List<RoomResponse> getAvailableRoomsSortedByPrice(String sortOrder);
}