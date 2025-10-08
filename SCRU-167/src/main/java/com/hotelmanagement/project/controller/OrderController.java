package com.hotelmanagement.project.controller;

import com.hotelmanagement.project.dto.OrderRequest;
import com.hotelmanagement.project.model.Order;
import com.hotelmanagement.project.model.OrderStatus;
import com.hotelmanagement.project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }

    @GetMapping
    public List<Order> list(@RequestParam(required = false) OrderStatus status,
                            @RequestParam(required = false) String room,
                            @RequestParam(required = false, name="q") String search) {
        return service.list(status, room, search);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid OrderRequest r) {
        return ResponseEntity.ok(service.create(r));
    }

    @PatchMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody OrderRequest r) {
        return service.update(id, r);
    }

    @PatchMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
