package com.hotelmanagement.scrum11.services;

import com.hotelmanagement.scrum11.dto.RoomRequest;
import com.hotelmanagement.scrum11.rooms.Room;
import com.hotelmanagement.scrum11.rooms.RoomRepository;
import com.hotelmanagement.scrum11.rooms.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository repo;

    public Room create(RoomRequest r){
        Room room = Room.builder()
                .name(r.name())
                .type(r.type())
                .price(r.price())
                .available(r.available())
                .rating(Optional.ofNullable(r.rating()).orElse(0.0))
                .description(r.description())
                .imageUrl(r.imageUrl())
                .build();
        return repo.save(room);
    }

    public Room update(Long id, RoomRequest r){
        Room room = repo.findById(id).orElseThrow(() ->
                new NoSuchElementException("Room not found: " + id));
        room.setName(r.name());
        room.setType(r.type());
        room.setPrice(r.price());
        room.setAvailable(r.available());
        room.setRating(Optional.ofNullable(r.rating()).orElse(0.0));
        room.setDescription(r.description());
        room.setImageUrl(r.imageUrl());
        return repo.save(room);
    }

    public void delete(Long id){ repo.deleteById(id); }

    public Room get(Long id){
        return repo.findById(id).orElseThrow(() ->
                new NoSuchElementException("Room not found: " + id));
    }

    public Page<Room> search(
            String q,
            RoomType type,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Double minRating,
            Pageable pageable
    ){
        // ✅ DO NOT use raw type: Specification spec = Specification.where(null)  (causes your “S not within its bound” error)
        Specification<Room> spec = Specification.allOf(); // null-safe, non-deprecated

        if (q != null && !q.isBlank()) {
            String like = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like)
            ));
        }
        if (type != null)     spec = spec.and((r, cq, cb) -> cb.equal(r.get("type"), type));
        if (minPrice != null) spec = spec.and((r, cq, cb) -> cb.ge(r.get("price"), minPrice));
        if (maxPrice != null) spec = spec.and((r, cq, cb) -> cb.le(r.get("price"), maxPrice));
        if (minRating != null)spec = spec.and((r, cq, cb) -> cb.ge(r.get("rating"), minRating));

        return repo.findAll(spec, pageable);  // ✔️ works because repo extends JpaSpecificationExecutor<Room>
    }
}
