package com.hotelmanagement.controller;


import com.hotelmanagement.dto.RoomRequest;
import com.hotelmanagement.dto.RoomResponse;
import com.hotelmanagement.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Admin CRUD endpoints under /api/admin/rooms.
 * (Authentication/authorization not implemented here; integrate Spring Security if needed.)
 */
@RestController
@RequestMapping("/api/admin/rooms")
public class AdminRoomController {

    private final RoomService service;

    public AdminRoomController(RoomService service) {
        this.service = service;
    }

    /**
     * POST /api/admin/rooms
     */
    @Operation(summary = "Create a room (admin)")
    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody RoomRequest request,
                                               UriComponentsBuilder ucb) {
        RoomResponse created = service.create(request);
        return ResponseEntity
                .created(ucb.path("/api/rooms/{id}").buildAndExpand(created.getId()).toUri())
                .body(created);
    }

    /**
     * PUT /api/admin/rooms/{id}
     */
    @Operation(summary = "Update a room (admin)")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable @NotNull Long id,
                                               @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    /**
     * DELETE /api/admin/rooms/{id}
     */
    @Operation(summary = "Delete a room (admin)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
