// [file name]: FoodOrderService.java
package com.palmbeachresort.service.menu;

import com.palmbeachresort.dto.menu.FoodOrderRequest;
import com.palmbeachresort.dto.menu.FoodOrderResponse;
import com.palmbeachresort.dto.menu.OrderStatusUpdateRequest;
import com.palmbeachresort.entity.menu.FoodOrder;
import java.util.List;

public interface FoodOrderService {

    // Customer operations
    FoodOrderResponse placeOrder(Long Id, FoodOrderRequest orderRequest);
    List<FoodOrderResponse> getCustomerOrders(Long Id);
    FoodOrderResponse getCustomerOrder(Long orderId, Long Id);
    FoodOrderResponse cancelOrder(Long orderId, Long Id);

    // Admin operations
    List<FoodOrderResponse> getAllOrders();
    List<FoodOrderResponse> getOrdersByStatus(FoodOrder.OrderStatus status);
    FoodOrderResponse getOrderById(Long orderId);
    FoodOrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest statusUpdate);
    void deleteOrder(Long orderId);

    // Real-time tracking
    List<FoodOrderResponse> getActiveKitchenOrders();
    FoodOrderResponse getOrderByNumber(String orderNumber);

    // Statistics
    Long getOrderCountByStatus(FoodOrder.OrderStatus status);
}