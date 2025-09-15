package com.hotelmanagement.scrum10.service;


import com.hotelmanagement.scrum10.dto.RoomDTO;
import com.hotelmanagement.scrum10.entity.Room;
import com.hotelmanagement.scrum10.entity.Room.RoomType;
import com.hotelmanagement.scrum10.repository.HotelRepository;
import com.hotelmanagement.scrum10.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RoomDTO> getRoomById(Long id) {
        return roomRepository.findById(id).map(this::toDTO);
    }

    public RoomDTO createRoom(RoomDTO dto) {
        Room room = toEntity(dto);
        hotelRepository.findById(dto.getHotelId()).ifPresent(room::setHotel);
        return toDTO(roomRepository.save(room));
    }

    public Optional<RoomDTO> updateRoom(Long id, RoomDTO dto) {
        return roomRepository.findById(id).map(room -> {
            room.setRoomNumber(dto.getRoomNumber());
            room.setType(RoomType.valueOf(dto.getType()));
            room.setAvailable(dto.isAvailable());
            return toDTO(roomRepository.save(room));
        });
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    private RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setHotelId(room.getHotel().getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType().name());
        dto.setAvailable(room.isAvailable());
        return dto;
    }

    private Room toEntity(RoomDTO dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setType(RoomType.valueOf(dto.getType()));
        room.setAvailable(dto.isAvailable());
        return room;
    }
}