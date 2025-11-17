// [file name]: AdminFoodOrderController.java
package com.palmbeachresort.controller.menu;

import com.palmbeachresort.dto.menu.FoodOrderResponse;
import com.palmbeachresort.dto.menu.OrderStatusUpdateRequest;
import com.palmbeachresort.entity.menu.FoodOrder;
import com.palmbeachresort.service.menu.FoodOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "https://frontend-palmbeachresort.vercel.app", allowCredentials = "true")
public class AdminFoodOrderController {

    @Autowired
    private FoodOrderService foodOrderService;

    /**
     * Get all food orders
     * GET /api/admin/orders
     */
    @GetMapping
    public ResponseEntity<List<FoodOrderResponse>> getAllOrders() {
        List<FoodOrderResponse> orders = foodOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order by ID
     * GET /api/admin/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FoodOrderResponse> getOrderById(@PathVariable Long id) {
        FoodOrderResponse order = foodOrderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Get orders by status
     * GET /api/admin/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FoodOrderResponse>> getOrdersByStatus(@PathVariable FoodOrder.OrderStatus status) {
        List<FoodOrderResponse> orders = foodOrderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Update order status
     * PATCH /api/admin/orders/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<FoodOrderResponse> updateOrderStatus(@PathVariable Long id,
                                                               @Valid @RequestBody OrderStatusUpdateRequest statusUpdate) {
        FoodOrderResponse updatedOrder = foodOrderService.updateOrderStatus(id, statusUpdate);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Delete an order
     * DELETE /api/admin/orders/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        foodOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get active kitchen orders
     * GET /api/admin/orders/kitchen/active
     */
    @GetMapping("/kitchen/active")
    public ResponseEntity<List<FoodOrderResponse>> getActiveKitchenOrders() {
        List<FoodOrderResponse> orders = foodOrderService.getActiveKitchenOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order statistics
     * GET /api/admin/orders/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getOrderStatistics() {
        Long pendingCount = foodOrderService.getOrderCountByStatus(FoodOrder.OrderStatus.PENDING);
        Long confirmedCount = foodOrderService.getOrderCountByStatus(FoodOrder.OrderStatus.CONFIRMED);
        Long preparingCount = foodOrderService.getOrderCountByStatus(FoodOrder.OrderStatus.PREPARING);
        Long readyCount = foodOrderService.getOrderCountByStatus(FoodOrder.OrderStatus.READY_FOR_DELIVERY);
        Long outForDeliveryCount = foodOrderService.getOrderCountByStatus(FoodOrder.OrderStatus.OUT_FOR_DELIVERY);
        Long deliveredCount = foodOrderService.getOrderCountByStatus(FoodOrder.OrderStatus.DELIVERED);
        Long cancelledCount = foodOrderService.getOrderCountByStatus(FoodOrder.OrderStatus.CANCELLED);

        return ResponseEntity.ok().body(new Object() {
            public final Long pending = pendingCount;
            public final Long confirmed = confirmedCount;
            public final Long preparing = preparingCount;
            public final Long readyForDelivery = readyCount;
            public final Long outForDelivery = outForDeliveryCount;
            public final Long delivered = deliveredCount;
            public final Long cancelled = cancelledCount;
            public final Long total = pendingCount + confirmedCount + preparingCount +
                    readyCount + outForDeliveryCount + deliveredCount + cancelledCount;
        });
    }
}