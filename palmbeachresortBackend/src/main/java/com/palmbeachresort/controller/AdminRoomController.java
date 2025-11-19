package com.palmbeachresort.controller;

import com.palmbeachresort.dto.RoomRequest;
import com.palmbeachresort.dto.RoomResponse;
import com.palmbeachresort.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/rooms")
@CrossOrigin(origins = {"https://frontend-palmbeachresort.vercel.app", "http://localhost:3000"},
        allowCredentials = "true")
public class AdminRoomController {

    @Autowired
    private RoomService roomService;

    /**
     * Create a new room
     * POST /api/admin/rooms
     */
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest roomRequest) {
        RoomResponse createdRoom = roomService.createRoom(roomRequest);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    /**
     * Update entire room
     * PUT /api/admin/rooms/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest roomRequest) {
        RoomResponse updatedRoom = roomService.updateRoom(id, roomRequest);
        return ResponseEntity.ok(updatedRoom);
    }

    /**
     * Partial update of room
     * PATCH /api/admin/rooms/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoomPartial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        RoomResponse updatedRoom = roomService.updateRoomPartial(id, updates);
        return ResponseEntity.ok(updatedRoom);
    }

    /**
     * Delete a room
     * DELETE /api/admin/rooms/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all rooms (including unavailable ones - Admin only)
     * GET /api/admin/rooms
     */
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get room by ID (Admin view - can see unavailable rooms)
     * GET /api/admin/rooms/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        RoomResponse room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    /**
     * Get room by room number (Admin view)
     * GET /api/admin/rooms/number/{roomNumber}
     */
    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<RoomResponse> getRoomByNumber(@PathVariable String roomNumber) {
        RoomResponse room = roomService.getRoomByRoomNumber(roomNumber);
        return ResponseEntity.ok(room);
    }

    /**
     * Check if room number exists
     * GET /api/admin/rooms/check/{roomNumber}
     */
    @GetMapping("/check/{roomNumber}")
    public ResponseEntity<Boolean> checkRoomNumberExists(@PathVariable String roomNumber) {
        boolean exists = roomService.existsByRoomNumber(roomNumber);
        return ResponseEntity.ok(exists);
    }
}