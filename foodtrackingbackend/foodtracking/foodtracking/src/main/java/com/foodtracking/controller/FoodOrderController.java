package com.foodtracking.controller;

import com.foodtracking.dto.FoodOrderDTO;
import com.foodtracking.dto.OrderSummaryDTO;
import com.foodtracking.model.OrderStatus;
import com.foodtracking.service.FoodOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    @Autowired
    public FoodOrderController(FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @GetMapping
    public ResponseEntity<List<FoodOrderDTO>> getAllOrders() {
        List<FoodOrderDTO> orders = foodOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodOrderDTO> getOrderById(@PathVariable String id) {
        FoodOrderDTO order = foodOrderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FoodOrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<FoodOrderDTO> orders = foodOrderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<FoodOrderDTO> createOrder(@RequestBody FoodOrderDTO orderDTO) {
        FoodOrderDTO createdOrder = foodOrderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodOrderDTO> updateOrder(@PathVariable String id, @RequestBody FoodOrderDTO orderDTO) {
        FoodOrderDTO updatedOrder = foodOrderService.updateOrder(id, orderDTO);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FoodOrderDTO> updateOrderStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status) {
        FoodOrderDTO updatedOrder = foodOrderService.updateOrderStatus(id, status);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        foodOrderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<OrderSummaryDTO> getOrderSummary() {
        OrderSummaryDTO summary = foodOrderService.getOrderSummary();
        return ResponseEntity.ok(summary);
    }
}