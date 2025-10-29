package com.palmbeachresort.service.impl;

import com.palmbeachresort.dto.RoomRequest;
import com.palmbeachresort.dto.RoomResponse;
import com.palmbeachresort.entity.Room;
import com.palmbeachresort.repository.RoomRepository;
import com.palmbeachresort.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {
        // Check if room number already exists
        if (roomRepository.existsByRoomNumber(roomRequest.getRoomNumber())) {
            throw new IllegalArgumentException("Room with number " + roomRequest.getRoomNumber() + " already exists");
        }

        Room room = new Room(
                roomRequest.getRoomNumber(),
                roomRequest.getRoomType(),
                roomRequest.getPrice(),
                roomRequest.getCapacity(),
                roomRequest.getDescription(),
                roomRequest.getAvailable(),
                roomRequest.getImageUrl()
        );

        // Auto-save to MySQL database
        Room savedRoom = roomRepository.save(room);
        return convertToResponse(savedRoom);
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest roomRequest) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));

        // Check if room number is being changed and if it already exists
        if (!existingRoom.getRoomNumber().equals(roomRequest.getRoomNumber()) &&
                roomRepository.existsByRoomNumber(roomRequest.getRoomNumber())) {
            throw new IllegalArgumentException("Room with number " + roomRequest.getRoomNumber() + " already exists");
        }

        // Update all fields - Auto-save to MySQL
        existingRoom.setRoomNumber(roomRequest.getRoomNumber());
        existingRoom.setRoomType(roomRequest.getRoomType());
        existingRoom.setPrice(roomRequest.getPrice());
        existingRoom.setCapacity(roomRequest.getCapacity());
        existingRoom.setDescription(roomRequest.getDescription());
        existingRoom.setAvailable(roomRequest.getAvailable());
        existingRoom.setImageUrl(roomRequest.getImageUrl());

        Room updatedRoom = roomRepository.save(existingRoom);
        return convertToResponse(updatedRoom);
    }

    @Override
    public RoomResponse updateRoomPartial(Long id, Map<String, Object> updates) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));

        // Handle each field update
        updates.forEach((key, value) -> {
            switch (key) {
                case "roomNumber":
                    String newRoomNumber = (String) value;
                    if (!existingRoom.getRoomNumber().equals(newRoomNumber) &&
                            roomRepository.existsByRoomNumber(newRoomNumber)) {
                        throw new IllegalArgumentException("Room with number " + newRoomNumber + " already exists");
                    }
                    existingRoom.setRoomNumber(newRoomNumber);
                    break;
                case "roomType":
                    existingRoom.setRoomType((String) value);
                    break;
                case "price":
                    existingRoom.setPrice(new BigDecimal(value.toString()));
                    break;
                case "capacity":
                    existingRoom.setCapacity((Integer) value);
                    break;
                case "description":
                    existingRoom.setDescription((String) value);
                    break;
                case "available":
                    existingRoom.setAvailable((Boolean) value);
                    break;
                case "imageUrl":
                    existingRoom.setImageUrl((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        // Auto-save partial updates to MySQL
        Room updatedRoom = roomRepository.save(existingRoom);
        return convertToResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Room not found with id: " + id);
        }
        // Auto-delete from MySQL
        roomRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRooms() {
        return roomRepository.findByAvailableTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByType(String roomType) {
        return roomRepository.findByRoomTypeAndAvailableTrue(roomType).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return roomRepository.findByPriceBetweenAndAvailableTrue(minPrice, maxPrice).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByCapacity(Integer capacity) {
        return roomRepository.findByCapacityGreaterThanEqualAndAvailableTrue(capacity).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> searchRooms(String roomType, BigDecimal minPrice, BigDecimal maxPrice, Integer capacity) {
        List<Room> rooms;

        if (roomType != null && minPrice != null && maxPrice != null) {
            rooms = roomRepository.findAvailableRoomsByTypeAndPriceRange(roomType, minPrice, maxPrice);
        } else if (roomType != null) {
            rooms = roomRepository.findByRoomTypeAndAvailableTrue(roomType);
        } else if (minPrice != null && maxPrice != null) {
            rooms = roomRepository.findByPriceBetweenAndAvailableTrue(minPrice, maxPrice);
        } else if (capacity != null) {
            rooms = roomRepository.findByCapacityGreaterThanEqualAndAvailableTrue(capacity);
        } else {
            rooms = roomRepository.findByAvailableTrue();
        }

        return rooms.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAvailableRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
        return convertToResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoomNumber(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomByRoomNumber(String roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with number: " + roomNumber));
        return convertToResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRoomsSortedByPrice(String sortOrder) {
        List<Room> rooms;
        if ("desc".equalsIgnoreCase(sortOrder)) {
            rooms = roomRepository.findAvailableRoomsOrderByPriceDesc();
        } else {
            rooms = roomRepository.findAvailableRoomsOrderByPriceAsc();
        }
        return rooms.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private RoomResponse convertToResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPrice(),
                room.getCapacity(),
                room.getDescription(),
                room.getAvailable(),
                room.getImageUrl(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}