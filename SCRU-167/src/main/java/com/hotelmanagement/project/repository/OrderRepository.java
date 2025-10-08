package com.hotelmanagement.project.repository;

import com.hotelmanagement.project.model.Order;
import com.hotelmanagement.project.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByRoomNumber(String roomNumber);
    List<Order> findByCustomerNameContainingIgnoreCase(String name);

    @Query("select o from Order o where (:status is null or o.status = :status) and " +
           "(:room is null or o.roomNumber = :room) and " +
           "(:q is null or lower(o.item) like lower(concat('%',:q,'%')) or lower(o.customerName) like lower(concat('%',:q,'%'))) " +
           "order by o.orderTime desc")
    List<Order> search(@Param("status") OrderStatus status,
                       @Param("room") String room,
                       @Param("q") String q);
}
