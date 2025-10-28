package org.example.booking_management_backend1.controller;


import org.example.booking_management_backend1.dto.ApiResponse;
import org.example.booking_management_backend1.model.Room;
import org.example.booking_management_backend1.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRooms() {
        try {
            List<Room> rooms = roomService.getAllRooms();
            return ResponseEntity.ok(
                    new ApiResponse(true, "Rooms retrieved successfully", rooms)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "Error retrieving rooms: " + e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableRooms() {
        try {
            List<Room> rooms = roomService.getAvailableRooms();
            return ResponseEntity.ok(
                    new ApiResponse(true, "Available rooms retrieved successfully", rooms)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "Error retrieving available rooms: " + e.getMessage()));
        }
    }

    @GetMapping("/available-for-dates")
    public ResponseEntity<ApiResponse> getAvailableRoomsForDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        try {
            List<Room> rooms = roomService.getAvailableRoomsForDates(checkInDate, checkOutDate);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Available rooms for dates retrieved successfully", rooms)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "Error retrieving available rooms for dates: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Room retrieved successfully", room)
            );
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}