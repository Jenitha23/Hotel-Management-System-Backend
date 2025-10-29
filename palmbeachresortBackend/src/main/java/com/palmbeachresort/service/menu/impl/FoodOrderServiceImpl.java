// [file name]: FoodOrderServiceImpl.java
package com.palmbeachresort.service.menu.impl;

import com.palmbeachresort.dto.menu.*;
import com.palmbeachresort.entity.menu.*;
import com.palmbeachresort.entity.auth.Customer;
import com.palmbeachresort.repository.menu.FoodOrderRepository;
import com.palmbeachresort.repository.menu.MenuItemRepository;
import com.palmbeachresort.repository.auth.CustomerRepository;
import com.palmbeachresort.service.menu.FoodOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FoodOrderServiceImpl implements FoodOrderService {

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public FoodOrderResponse placeOrder(Long customerId, FoodOrderRequest orderRequest) {
        try {
            System.out.println("🔔 Starting order placement for customer: " + customerId);

            // Validate customer
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

            System.out.println("✅ Found customer: " + customer.getFullName());

            // Get customer's cart with null safety
            CartResponse cartResponse = cartService.getCartWithDetails(customerId);

            // NULL SAFETY CHECKS
            if (cartResponse == null) {
                throw new IllegalArgumentException("Cart service returned null response");
            }

            List<CartItemResponse> cartItems = cartResponse.getItems();
            if (cartItems == null) {
                cartItems = Collections.emptyList();
            }

            if (cartItems.isEmpty()) {
                throw new IllegalArgumentException("Cannot place order with empty cart");
            }

            System.out.println("🛒 Cart items count: " + cartItems.size());

            // Create order
            String orderNumber = generateOrderNumber();
            System.out.println("📦 Generated order number: " + orderNumber);

            FoodOrder order = new FoodOrder(orderNumber, customer,
                    orderRequest.getSpecialInstructions(), orderRequest.getRoomNumber());

            // Convert cart items to order items with validation
            List<OrderItem> validOrderItems = new ArrayList<>();
            for (CartItemResponse cartItem : cartItems) {
                if (cartItem == null) {
                    System.out.println("⚠️ Warning: Found null cart item, skipping");
                    continue;
                }

                try {
                    System.out.println("🍕 Processing cart item: " + cartItem.getMenuItemName() + " (ID: " + cartItem.getMenuItemId() + ")");

                    MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId())
                            .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + cartItem.getMenuItemId()));

                    OrderItem orderItem = new OrderItem(order, menuItem, cartItem.getQuantity());
                    validOrderItems.add(orderItem);

                } catch (Exception e) {
                    System.err.println("❌ Error processing cart item " + cartItem.getMenuItemId() + ": " + e.getMessage());
                    // Continue with other items instead of failing entire order
                }
            }

            // Check if any valid items were added
            if (validOrderItems.isEmpty()) {
                throw new IllegalArgumentException("No valid items found in cart to place order");
            }

            // Add all valid items to order
            for (OrderItem orderItem : validOrderItems) {
                order.addItem(orderItem);
            }

            FoodOrder savedOrder = foodOrderRepository.save(order);
            System.out.println("✅ Order saved with ID: " + savedOrder.getId());

            // Clear the cart after successful order
            try {
                cartService.clearCart(customerId);
                System.out.println("🗑️ Cart cleared successfully");
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Could not clear cart: " + e.getMessage());
                // Don't fail the order if cart clearing fails
            }

            // Send real-time notification
            try {
                sendOrderUpdateNotification(savedOrder);
                System.out.println("📢 Order notification sent");
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Could not send notification: " + e.getMessage());
                // Don't fail the order if notification fails
            }

            return convertToResponse(savedOrder);

        } catch (Exception e) {
            System.err.println("❌ Error placing order: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to place order: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodOrderResponse> getCustomerOrders(Long customerId) {
        try {
            List<FoodOrder> orders = foodOrderRepository.findByCustomerIdWithItems(customerId);
            return orders.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting customer orders: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FoodOrderResponse getCustomerOrder(Long orderId, Long customerId) {
        try {
            FoodOrder order = foodOrderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

            if (!order.getCustomer().getId().equals(customerId)) {
                throw new IllegalArgumentException("Access denied - order does not belong to customer");
            }

            return convertToResponse(order);
        } catch (Exception e) {
            System.err.println("❌ Error getting customer order: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve order: " + e.getMessage());
        }
    }

    @Override
    public FoodOrderResponse cancelOrder(Long orderId, Long customerId) {
        try {
            FoodOrder order = foodOrderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

            if (!order.getCustomer().getId().equals(customerId)) {
                throw new IllegalArgumentException("Access denied - order does not belong to customer");
            }

            // Check if order can be cancelled
            if (!canCancelOrder(order)) {
                throw new IllegalArgumentException("Order cannot be cancelled at this stage");
            }

            order.setStatus(FoodOrder.OrderStatus.CANCELLED);
            FoodOrder cancelledOrder = foodOrderRepository.save(order);

            // Send real-time notification
            try {
                sendOrderUpdateNotification(cancelledOrder);
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Could not send cancellation notification: " + e.getMessage());
            }

            return convertToResponse(cancelledOrder);
        } catch (Exception e) {
            System.err.println("❌ Error cancelling order: " + e.getMessage());
            throw new RuntimeException("Failed to cancel order: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodOrderResponse> getAllOrders() {
        try {
            return foodOrderRepository.findAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting all orders: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodOrderResponse> getOrdersByStatus(FoodOrder.OrderStatus status) {
        try {
            List<FoodOrder> orders = foodOrderRepository.findByStatusOrderByCreatedAtDesc(status);
            return orders.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting orders by status: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FoodOrderResponse getOrderById(Long orderId) {
        try {
            FoodOrder order = foodOrderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
            return convertToResponse(order);
        } catch (Exception e) {
            System.err.println("❌ Error getting order by ID: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve order: " + e.getMessage());
        }
    }

    @Override
    public FoodOrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest statusUpdate) {
        try {
            FoodOrder order = foodOrderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

            FoodOrder.OrderStatus oldStatus = order.getStatus();
            order.setStatus(statusUpdate.getStatus());

            FoodOrder updatedOrder = foodOrderRepository.save(order);

            // Send real-time notification to customer
            try {
                sendOrderUpdateNotification(updatedOrder);
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Could not send status update notification: " + e.getMessage());
            }

            return convertToResponse(updatedOrder);
        } catch (Exception e) {
            System.err.println("❌ Error updating order status: " + e.getMessage());
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        try {
            if (!foodOrderRepository.existsById(orderId)) {
                throw new IllegalArgumentException("Order not found with id: " + orderId);
            }
            foodOrderRepository.deleteById(orderId);
        } catch (Exception e) {
            System.err.println("❌ Error deleting order: " + e.getMessage());
            throw new RuntimeException("Failed to delete order: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodOrderResponse> getActiveKitchenOrders() {
        try {
            List<FoodOrder> orders = foodOrderRepository.findActiveKitchenOrders();
            return orders.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error getting active kitchen orders: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FoodOrderResponse getOrderByNumber(String orderNumber) {
        try {
            FoodOrder order = foodOrderRepository.findByOrderNumber(orderNumber)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with number: " + orderNumber));
            return convertToResponse(order);
        } catch (Exception e) {
            System.err.println("❌ Error getting order by number: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve order: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getOrderCountByStatus(FoodOrder.OrderStatus status) {
        try {
            return foodOrderRepository.countByStatus(status);
        } catch (Exception e) {
            System.err.println("❌ Error getting order count by status: " + e.getMessage());
            return 0L;
        }
    }

    // Utility methods
    private String generateOrderNumber() {
        return "FOD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean canCancelOrder(FoodOrder order) {
        return order.getStatus() == FoodOrder.OrderStatus.PENDING ||
                order.getStatus() == FoodOrder.OrderStatus.CONFIRMED;
    }

    private void sendOrderUpdateNotification(FoodOrder order) {
        try {
            FoodOrderResponse response = convertToResponse(order);

            // Notify customer
            messagingTemplate.convertAndSendToUser(
                    order.getCustomer().getId().toString(),
                    "/queue/order-updates",
                    response
            );

            // Notify admin/staff
            messagingTemplate.convertAndSend("/topic/admin/order-updates", response);
        } catch (Exception e) {
            System.err.println("❌ Error sending notification: " + e.getMessage());
            // Don't throw exception - notification failure shouldn't break order flow
        }
    }

    private FoodOrderResponse convertToResponse(FoodOrder order) {
        try {
            // NULL SAFETY for items list
            List<OrderItemResponse> itemResponses = order.getItems() == null
                    ? Collections.emptyList()
                    : order.getItems().stream()
                    .map(this::convertOrderItemToResponse)
                    .collect(Collectors.toList());

            boolean canCancel = canCancelOrder(order);

            return new FoodOrderResponse(
                    order.getId(),
                    order.getOrderNumber(),
                    order.getCustomer().getId(),
                    order.getCustomer().getFullName(),
                    order.getCustomer().getEmail(),
                    itemResponses,
                    order.getTotalAmount() != null ? order.getTotalAmount() : java.math.BigDecimal.ZERO,
                    order.getStatus(),
                    order.getSpecialInstructions(),
                    order.getRoomNumber(),
                    order.getEstimatedPreparationTime() != null ? order.getEstimatedPreparationTime() : 30,
                    order.getCreatedAt() != null ? order.getCreatedAt() : LocalDateTime.now(),
                    order.getUpdatedAt() != null ? order.getUpdatedAt() : LocalDateTime.now(),
                    order.getCompletedAt(),
                    canCancel
            );
        } catch (Exception e) {
            System.err.println("❌ Error converting order to response: " + e.getMessage());
            throw new RuntimeException("Failed to convert order to response");
        }
    }

    private OrderItemResponse convertOrderItemToResponse(OrderItem orderItem) {
        try {
            return new OrderItemResponse(
                    orderItem.getId(),
                    orderItem.getMenuItem().getId(),
                    orderItem.getMenuItem().getName(),
                    orderItem.getMenuItem().getImageUrl(),
                    orderItem.getUnitPrice() != null ? orderItem.getUnitPrice() : java.math.BigDecimal.ZERO,
                    orderItem.getQuantity() != null ? orderItem.getQuantity() : 0,
                    orderItem.getSubtotal() != null ? orderItem.getSubtotal() : java.math.BigDecimal.ZERO
            );
        } catch (Exception e) {
            System.err.println("❌ Error converting order item to response: " + e.getMessage());
            // Return a safe default response
            return new OrderItemResponse(
                    orderItem != null ? orderItem.getId() : 0L,
                    0L,
                    "Unknown Item",
                    "",
                    java.math.BigDecimal.ZERO,
                    0,
                    java.math.BigDecimal.ZERO
            );
        }
    }
}