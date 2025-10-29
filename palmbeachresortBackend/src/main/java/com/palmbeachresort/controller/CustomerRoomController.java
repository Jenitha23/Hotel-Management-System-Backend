package com.palmbeachresort.controller;

import com.palmbeachresort.dto.RoomResponse;
import com.palmbeachresort.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/customer/rooms")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerRoomController {

    @Autowired
    private RoomService roomService;

    /**
     * Get all available rooms
     * GET /api/customer/rooms
     */
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAvailableRooms() {
        List<RoomResponse> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get available room by ID
     * GET /api/customer/rooms/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getAvailableRoomById(@PathVariable Long id) {
        RoomResponse room = roomService.getRoomById(id);
        // Check if room is available
        if (!room.getAvailable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }

    /**
     * Get rooms by type (only available)
     * GET /api/customer/rooms/type/{roomType}
     */
    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<RoomResponse>> getRoomsByType(@PathVariable String roomType) {
        List<RoomResponse> rooms = roomService.getRoomsByType(roomType);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by price range (only available)
     * GET /api/customer/rooms/price-range?minPrice=100&maxPrice=300
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<RoomResponse>> getRoomsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<RoomResponse> rooms = roomService.getRoomsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get rooms by capacity (only available)
     * GET /api/customer/rooms/capacity/{capacity}
     */
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<RoomResponse>> getRoomsByCapacity(@PathVariable Integer capacity) {
        List<RoomResponse> rooms = roomService.getRoomsByCapacity(capacity);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Advanced search with multiple filters
     * GET /api/customer/rooms/search?roomType=DELUXE&minPrice=200&maxPrice=400&capacity=2
     */
    @GetMapping("/search")
    public ResponseEntity<List<RoomResponse>> searchRooms(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer capacity) {

        List<RoomResponse> rooms = roomService.searchRooms(roomType, minPrice, maxPrice, capacity);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get all available room types
     * GET /api/customer/rooms/types
     */
    @GetMapping("/types")
    public ResponseEntity<List<String>> getAvailableRoomTypes() {
        List<String> roomTypes = roomService.getAvailableRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    /**
     * Get available rooms sorted by price
     * GET /api/customer/rooms/sorted?sort=asc (or desc)
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<RoomResponse>> getAvailableRoomsSortedByPrice(
            @RequestParam(defaultValue = "asc") String sort) {
        List<RoomResponse> rooms = roomService.getAvailableRoomsSortedByPrice(sort);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get room by room number (only if available)
     * GET /api/customer/rooms/number/{roomNumber}
     */
    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<RoomResponse> getAvailableRoomByNumber(@PathVariable String roomNumber) {
        RoomResponse room = roomService.getRoomByRoomNumber(roomNumber);
        if (!room.getAvailable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }
}