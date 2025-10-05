package com.palmbeachresort.service;

import com.palmbeachresort.model.*;
import com.palmbeachresort.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    public Order createOrderFromCart(String sessionId, Order orderDetails) {
        Cart cart = cartService.getCartBySessionId(sessionId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Convert cart items to order items
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenu(cartItem.getMenu());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getMenu().getPrice());
            orderDetails.getItems().add(orderItem);
        }

        orderDetails.calculateTotals();
        Order savedOrder = orderRepository.save(orderDetails);

        // Clear the cart after order creation
        cartService.clearCart(sessionId);

        return savedOrder;
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}