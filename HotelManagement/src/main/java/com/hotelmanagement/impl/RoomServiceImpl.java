package com.hotelmanagement.impl;


import com.hotelmanagement.dto.RoomRequest;
import com.hotelmanagement.dto.RoomResponse;
import com.hotelmanagement.entity.Room;
import com.hotelmanagement.entity.RoomType;
import com.hotelmanagement.error.NotFoundException;
import com.hotelmanagement.repository.RoomRepository;
import com.hotelmanagement.service.RoomService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Default RoomService implementation.
 */
@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repo;

    public RoomServiceImpl(RoomRepository repo) {
        this.repo = repo;
    }

    @Override
    public RoomResponse create(RoomRequest request) {
        // Unique roomNumber enforced by DB; we also check early:
        repo.findByRoomNumber(request.getRoomNumber()).ifPresent(r -> {
            throw new DataIntegrityViolationException("roomNumber must be unique");
        });

        Room entity = toEntity(new Room(), request);
        Room saved = repo.save(entity);
        return toResponse(saved);
    }

    @Override
    public RoomResponse update(Long id, RoomRequest request) {
        Room existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Room id " + id + " not found"));

        // If roomNumber changes, ensure uniqueness
        if (!existing.getRoomNumber().equals(request.getRoomNumber())) {
            repo.findByRoomNumber(request.getRoomNumber()).ifPresent(r -> {
                throw new DataIntegrityViolationException("roomNumber must be unique");
            });
        }

        Room updated = toEntity(existing, request);
        return toResponse(repo.save(updated));
    }

    @Override
    public void delete(Long id) {
        Room existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Room id " + id + " not found"));
        repo.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getById(Long id) {
        Room room = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Room id " + id + " not found"));
        return toResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> list(Optional<Boolean> available, Optional<RoomType> type) {
        List<Room> rooms = switch ((available.isPresent() ? 1 : 0) * 2 + (type.isPresent() ? 1 : 0)) {
            case 0 -> repo.findAll();
            case 1 -> repo.findByType(type.get());
            case 2 -> repo.findByAvailable(available.get());
            case 3 -> repo.findByAvailableAndType(available.get(), type.get());
            default -> repo.findAll();
        };
        return rooms.stream().map(this::toResponse).toList();
    }

    // --- mapping helpers ---

    private Room toEntity(Room entity, RoomRequest req) {
        entity.setRoomNumber(req.getRoomNumber());
        entity.setType(req.getType());
        entity.setPricePerNight(req.getPricePerNight());
        entity.setCapacity(req.getCapacity());
        entity.setAvailable(req.isAvailable());
        entity.setDescription(req.getDescription());
        return entity;
    }

    private RoomResponse toResponse(Room r) {
        RoomResponse dto = new RoomResponse();
        dto.setId(r.getId());
        dto.setRoomNumber(r.getRoomNumber());
        dto.setType(r.getType());
        dto.setPricePerNight(r.getPricePerNight());
        dto.setCapacity(r.getCapacity());
        dto.setAvailable(r.isAvailable());
        dto.setDescription(r.getDescription());
        return dto;
    }
}
