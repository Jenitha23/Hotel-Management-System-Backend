package com.hotelmanagement.controller;

import com.hotelmanagement.dto.RoomResponse;
import com.hotelmanagement.entity.RoomType;
import com.hotelmanagement.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Public read-only endpoints under /api/rooms.
 */
@RestController
@RequestMapping("/api/rooms")
@Validated
public class PublicRoomController {

    private final RoomService service;

    public PublicRoomController(RoomService service) {
        this.service = service;
    }

    /**
     * GET /api/rooms?available=true&type=DOUBLE
     * Both filters are optional.
     */
    @Operation(summary = "List rooms (optionally filter by availability and type)")
    @GetMapping
    public ResponseEntity<List<RoomResponse>> list(
            @RequestParam Optional<Boolean> available,
            @RequestParam Optional<RoomType> type) {

        return ResponseEntity.ok(service.list(available, type));
    }

    /**
     * GET /api/rooms/{id}
     */
    @Operation(summary = "Get a room by id")
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> get(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
