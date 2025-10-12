package com.hotelmanagement.scru167.Services;


import com.hotelmanagement.scru167.domain.*;
import com.hotelmanagement.scru167.dto.BookingCreateRequest;
import com.hotelmanagement.scru167.Repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepo;
    private final CustomerRepository customerRepo;
    private final RoomRepository roomRepo;

    public BookingService(BookingRepository b, CustomerRepository c, RoomRepository r) {
        this.bookingRepo = b; this.customerRepo = c; this.roomRepo = r;
    }

    public Booking create(BookingCreateRequest req) {
        var cust = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        var room = roomRepo.findById(req.roomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        var b = new Booking();
        b.setCustomer(cust);
        b.setRoom(room);
        b.setCheckIn(req.checkIn());
        b.setCheckOut(req.checkOut());
        return bookingRepo.save(b);
    }

    public Booking get(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: "+id));
    }
}
