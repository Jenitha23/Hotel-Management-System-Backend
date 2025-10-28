package com.foodtracking.service;

import com.foodtracking.dto.FoodOrderDTO;
import com.foodtracking.dto.OrderSummaryDTO;
import com.foodtracking.model.FoodOrder;
import com.foodtracking.model.OrderStatus;
import com.foodtracking.repository.FoodOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    @Autowired
    public FoodOrderService(FoodOrderRepository foodOrderRepository) {
        this.foodOrderRepository = foodOrderRepository;
    }

    public List<FoodOrderDTO> getAllOrders() {
        return foodOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FoodOrderDTO getOrderById(String id) {
        return foodOrderRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<FoodOrderDTO> getOrdersByStatus(OrderStatus status) {
        return foodOrderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FoodOrderDTO createOrder(FoodOrderDTO orderDTO) {
        FoodOrder order = convertToEntity(orderDTO);
        FoodOrder savedOrder = foodOrderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    public FoodOrderDTO updateOrderStatus(String id, OrderStatus newStatus) {
        return foodOrderRepository.findById(id)
                .map(order -> {
                    order.setStatus(newStatus);
                    FoodOrder updatedOrder = foodOrderRepository.save(order);
                    return convertToDTO(updatedOrder);
                })
                .orElse(null);
    }

    public FoodOrderDTO updateOrder(String id, FoodOrderDTO orderDTO) {
        return foodOrderRepository.findById(id)
                .map(order -> {
                    order.setItem(orderDTO.getItem());
                    order.setQuantity(orderDTO.getQuantity());
                    order.setOrderTime(parseTime(orderDTO.getOrderTime()));
                    order.setEstimatedTime(parseTime(orderDTO.getEstimatedTime()));
                    order.setStatus(orderDTO.getStatus());
                    order.setLocation(orderDTO.getLocation());
                    FoodOrder updatedOrder = foodOrderRepository.save(order);
                    return convertToDTO(updatedOrder);
                })
                .orElse(null);
    }

    public void deleteOrder(String id) {
        foodOrderRepository.deleteById(id);
    }

    public OrderSummaryDTO getOrderSummary() {
        long preparing = foodOrderRepository.countByStatus(OrderStatus.PREPARING);
        long ready = foodOrderRepository.countByStatus(OrderStatus.DONE);
        long outForDelivery = foodOrderRepository.countByStatus(OrderStatus.READY_TO_DELIVER);
        long delivered = foodOrderRepository.countByStatus(OrderStatus.DELIVERED);

        return new OrderSummaryDTO(preparing, ready, outForDelivery, delivered);
    }

    private FoodOrderDTO convertToDTO(FoodOrder order) {
        return new FoodOrderDTO(
                order.getId(),
                order.getItem(),
                order.getQuantity(),
                formatTime(order.getOrderTime()),
                formatTime(order.getEstimatedTime()),
                order.getStatus(),
                order.getLocation()
        );
    }

    private FoodOrder convertToEntity(FoodOrderDTO dto) {
        FoodOrder order = new FoodOrder();
        order.setItem(dto.getItem());
        order.setQuantity(dto.getQuantity());
        order.setOrderTime(parseTime(dto.getOrderTime()));
        order.setEstimatedTime(parseTime(dto.getEstimatedTime()));
        order.setStatus(dto.getStatus());
        order.setLocation(dto.getLocation());
        return order;
    }

    private String formatTime(LocalTime time) {
        return time != null ? time.format(timeFormatter) : "";
    }

    private LocalTime parseTime(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeString.toUpperCase(), timeFormatter);
        } catch (Exception e) {
            // Try parsing with different formats if needed
            return LocalTime.now();
        }
    }

    // Method to initialize sample data
    @Transactional
    public void initializeSampleData() {
        if (foodOrderRepository.count() == 0) {
            List<FoodOrder> sampleOrders = List.of(
                    new FoodOrder("Sunset Dinner Special", 1,
                            LocalTime.of(18, 30), LocalTime.of(19, 15),
                            OrderStatus.PREPARING, "Beachside Restaurant"),
                    new FoodOrder("Beach Bar & Appetizers", 1,
                            LocalTime.of(17, 45), LocalTime.of(18, 0),
                            OrderStatus.READY_TO_DELIVER, "Pool Bar"),
                    new FoodOrder("Tropical Breakfast Buffet", 2,
                            LocalTime.of(8, 0), LocalTime.of(8, 30),
                            OrderStatus.DELIVERED, "Main Restaurant"),
                    new FoodOrder("Poolside Lunch & Cocktails", 1,
                            LocalTime.of(12, 30), LocalTime.of(13, 0),
                            OrderStatus.DONE, "Pool Grill")
            );

            foodOrderRepository.saveAll(sampleOrders);
            System.out.println("Sample data initialized successfully!");
        }
    }
}