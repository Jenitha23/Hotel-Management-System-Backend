package com.palmbeachresort.service;

import com.palmbeachresort.dto.OrderDTO;
import com.palmbeachresort.dto.OrderStatsDTO;
import com.palmbeachresort.model.Order;
import com.palmbeachresort.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return new OrderDTO(order);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return new OrderDTO(savedOrder);
    }

    public OrderDTO updateOrder(String id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        updateOrderFromDTO(existingOrder, orderDTO);
        Order updatedOrder = orderRepository.save(existingOrder);
        return new OrderDTO(updatedOrder);
    }

    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public List<OrderDTO> searchOrders(String searchTerm) {
        return orderRepository.searchOrders(searchTerm).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        return orderRepository.findByStatus(orderStatus).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByPriority(String priority) {
        Order.Priority orderPriority = Order.Priority.valueOf(priority.toUpperCase());
        return orderRepository.findByPriority(orderPriority).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    public OrderStatsDTO getOrderStats() {
        long total = orderRepository.count();
        long ordered = orderRepository.countByStatus(Order.OrderStatus.ORDERED);
        long preparing = orderRepository.countByStatus(Order.OrderStatus.PREPARING);
        long done = orderRepository.countByStatus(Order.OrderStatus.DONE);
        long delivering = orderRepository.countByStatus(Order.OrderStatus.READY_TO_DELIVER);
        long delivered = orderRepository.countByStatus(Order.OrderStatus.DELIVERED);
        long urgent = orderRepository.countByPriority(Order.Priority.URGENT);

        return new OrderStatsDTO(total, ordered, preparing, done, delivering, delivered, urgent);
    }

    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        updateOrderFromDTO(order, dto);
        return order;
    }

    private void updateOrderFromDTO(Order order, OrderDTO dto) {
        order.setCustomerName(dto.getCustomerName());
        order.setRoomNumber(dto.getRoomNumber());
        order.setItem(dto.getItem());
        order.setQuantity(dto.getQuantity());
        order.setOrderTime(dto.getOrderTime());
        order.setEstimatedTime(dto.getEstimatedTime());
        order.setLocation(dto.getLocation());
        order.setSpecialInstructions(dto.getSpecialInstructions());

        if (dto.getStatus() != null) {
            order.setStatus(Order.OrderStatus.valueOf(dto.getStatus().toUpperCase()));
        }

        if (dto.getPriority() != null) {
            order.setPriority(Order.Priority.valueOf(dto.getPriority().toUpperCase()));
        }
    }
}