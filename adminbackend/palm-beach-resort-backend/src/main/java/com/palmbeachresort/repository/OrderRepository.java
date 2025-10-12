package com.palmbeachresort.repository;

import com.palmbeachresort.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByStatus(Order.OrderStatus status);

    List<Order> findByPriority(Order.Priority priority);

    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);

    List<Order> findByRoomNumber(String roomNumber);

    List<Order> findByItemContainingIgnoreCase(String item);

    @Query("SELECT o FROM Order o WHERE " +
            "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "o.roomNumber LIKE CONCAT('%', :searchTerm, '%') OR " +
            "LOWER(o.item) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "o.id LIKE CONCAT('%', :searchTerm, '%')")
    List<Order> searchOrders(String searchTerm);

    long countByStatus(Order.OrderStatus status);

    long countByPriority(Order.Priority priority);
}