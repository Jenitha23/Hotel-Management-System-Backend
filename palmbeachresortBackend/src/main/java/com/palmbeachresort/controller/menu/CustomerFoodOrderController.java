// [file name]: CustomerFoodOrderController.java
package com.palmbeachresort.controller.menu;

import com.palmbeachresort.dto.menu.FoodOrderRequest;
import com.palmbeachresort.dto.menu.FoodOrderResponse;
import com.palmbeachresort.service.menu.FoodOrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerFoodOrderController {

    @Autowired
    private FoodOrderService foodOrderService;

    /**
     * Place a new food order
     * POST /api/customer/orders
     */
    @PostMapping
    public ResponseEntity<FoodOrderResponse> placeOrder(@Valid @RequestBody FoodOrderRequest orderRequest,
                                                        HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        FoodOrderResponse order = foodOrderService.placeOrder(customerId, orderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * Get all orders for current customer
     * GET /api/customer/orders
     */
    @GetMapping
    public ResponseEntity<List<FoodOrderResponse>> getCustomerOrders(HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<FoodOrderResponse> orders = foodOrderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get specific order by ID
     * GET /api/customer/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FoodOrderResponse> getCustomerOrder(@PathVariable Long id, HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        FoodOrderResponse order = foodOrderService.getCustomerOrder(id, customerId);
        return ResponseEntity.ok(order);
    }

    /**
     * Cancel an order
     * POST /api/customer/orders/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<FoodOrderResponse> cancelOrder(@PathVariable Long id, HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        FoodOrderResponse cancelledOrder = foodOrderService.cancelOrder(id, customerId);
        return ResponseEntity.ok(cancelledOrder);
    }

    /**
     * Track order by order number
     * GET /api/customer/orders/track/{orderNumber}
     */
    @GetMapping("/track/{orderNumber}")
    public ResponseEntity<FoodOrderResponse> trackOrder(@PathVariable String orderNumber) {
        FoodOrderResponse order = foodOrderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
}