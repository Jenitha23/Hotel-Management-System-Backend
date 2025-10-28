package com.hotelmanagement.scru167.Repositories;


import com.hotelmanagement.scru167.domain.OrderItem;
import com.hotelmanagement.scru167.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByBooking(Booking booking);
}
