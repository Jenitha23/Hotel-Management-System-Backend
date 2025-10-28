package com.hotelmanagement.scru167.controller;


import com.hotelmanagement.scru167.domain.OrderItem;
import com.hotelmanagement.scru167.dto.OrderItemRequest;
import com.hotelmanagement.scru167.Services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService s) { this.orderService = s; }

    @PostMapping
    public ResponseEntity<OrderItem> add(@RequestBody @Valid OrderItemRequest req) {
        return ResponseEntity.ok(orderService.addItem(req));
    }
}
