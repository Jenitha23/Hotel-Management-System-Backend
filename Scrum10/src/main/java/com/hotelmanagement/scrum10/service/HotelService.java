package com.hotelmanagement.scrum10.service;


import com.hotelmanagement.scrum10.dto.HotelDTO;
import com.hotelmanagement.scrum10.entity.Hotel;
import com.hotelmanagement.scrum10.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<HotelDTO> getHotelById(Long id) {
        return hotelRepository.findById(id).map(this::toDTO);
    }

    public HotelDTO createHotel(HotelDTO dto) {
        Hotel hotel = toEntity(dto);
        return toDTO(hotelRepository.save(hotel));
    }

    public Optional<HotelDTO> updateHotel(Long id, HotelDTO dto) {
        return hotelRepository.findById(id).map(hotel -> {
            hotel.setName(dto.getName());
            hotel.setLocation(dto.getLocation());
            return toDTO(hotelRepository.save(hotel));
        });
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    private HotelDTO toDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setLocation(hotel.getLocation());
        return dto;
    }

    private Hotel toEntity(HotelDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setLocation(dto.getLocation());
        return hotel;
    }
}