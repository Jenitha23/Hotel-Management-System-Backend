package com.hotelmanagement.service;

import com.hotelmanagement.dto.RoomRequest;
import com.hotelmanagement.dto.RoomResponse;
import com.hotelmanagement.entity.RoomType;

import java.util.List;
import java.util.Optional;

/**
 * Business service for managing rooms.
 */
public interface RoomService {

    /**
     * Creates a new room and returns its representation.
     */
    RoomResponse create(RoomRequest request);

    /**
     * Updates an existing room by id.
     */
    RoomResponse update(Long id, RoomRequest request);

    /**
     * Deletes a room by id.
     */
    void delete(Long id);

    /**
     * Returns a room by id or throws if not found.
     */
    RoomResponse getById(Long id);

    /**
     * Lists rooms, optionally filtered by availability and/or type.
     */
    List<RoomResponse> list(Optional<Boolean> available, Optional<RoomType> type);
}
