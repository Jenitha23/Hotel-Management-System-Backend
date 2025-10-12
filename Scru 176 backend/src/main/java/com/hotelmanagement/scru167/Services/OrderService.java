package com.hotelmanagement.scru167.Services;


import com.hotelmanagement.scru167.domain.*;
import com.hotelmanagement.scru167.dto.OrderItemRequest;
import com.hotelmanagement.scru167.Repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final BookingRepository bookingRepo;
    private final OrderItemRepository itemRepo;

    public OrderService(BookingRepository b, OrderItemRepository i) {
        this.bookingRepo = b; this.itemRepo = i;
    }

    public OrderItem addItem(OrderItemRequest req) {
        var booking = bookingRepo.findById(req.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        var item = new OrderItem();
        item.setBooking(booking);
        item.setType(OrderType.valueOf(req.type().toUpperCase()));
        item.setDescription(req.description());
        item.setQuantity(req.quantity());
        item.setUnitPrice(req.unitPrice());
        return itemRepo.save(item);
    }
}
