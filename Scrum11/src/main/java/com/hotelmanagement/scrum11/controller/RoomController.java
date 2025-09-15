package com.hotelmanagement.scrum11.controller;

import com.hotelmanagement.scrum11.dto.RoomRequest;
import com.hotelmanagement.scrum11.dto.RoomResponse;
import com.hotelmanagement.scrum11.rooms.RoomRepository;
import com.hotelmanagement.scrum11.rooms.RoomType;
import com.hotelmanagement.scrum11.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import com.hotelmanagement.scrum11.rooms.Room;


import java.math.BigDecimal;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "false")
public class RoomController {
    private final RoomService service;
    private final RoomRepository repo;

    public RoomController(RoomService service, RoomRepository repo) {
        this.service = service; this.repo = repo;
    }

    // Create (admin)
    @PostMapping
    public RoomResponse create(@RequestBody @Valid RoomRequest req){
        return toDto(service.create(req));
    }

    // Update (admin)
    @PutMapping("/{id}")
    public RoomResponse update(@PathVariable Long id, @RequestBody @Valid RoomRequest req){
        return toDto(service.update(id, req));
    }

    // Delete (admin)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ repo.deleteById(id); }

    // Get one
    @GetMapping("/{id}")
    public RoomResponse get(@PathVariable Long id){ return toDto(repo.findById(id).orElseThrow()); }

    // List (search/filter/paginate)
    @GetMapping("/search")
    public Page<RoomResponse> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "price,asc") String sort
    ){
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(s[1]), s[0]));
        return service.search(q, type, minPrice, maxPrice, minRating, pageable).map(this::toDto);
    }

    private RoomResponse toDto(Room r){
        return new RoomResponse(r.getId(), r.getName(), r.getType(), r.getPrice(), r.getAvailable(),
                r.getRating(), r.getDescription(), r.getImageUrl());
    }
}
