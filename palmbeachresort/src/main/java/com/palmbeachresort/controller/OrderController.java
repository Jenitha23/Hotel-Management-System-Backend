package com.palmbeachresort.controller;

import com.palmbeachresort.model.Order;
import com.palmbeachresort.service.OrderService;
import com.palmbeachresort.service.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5176")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestHeader("X-Session-Id") String sessionId,
            @RequestBody Order order) {
        Order createdOrder = orderService.createOrderFromCart(sessionId, order);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrdersByEmail(@RequestParam String email) {
        List<Order> orders = orderService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderNumber}/bill")
    public ResponseEntity<InputStreamResource> generateBill(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByNumber(orderNumber);
        ByteArrayInputStream bill = pdfGeneratorService.generateOrderBill(order);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=bill-" + orderNumber + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bill));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateStatusRequest request) {
        Order order = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(order);
    }

    public static class UpdateStatusRequest {
        private String status;

        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}