package org.example.booking_management_backend1.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.booking_management_backend1.dto.BookingRequest;
import org.example.booking_management_backend1.dto.BookingResponse;
import org.example.booking_management_backend1.model.Booking;
import org.example.booking_management_backend1.model.Customer;
import org.example.booking_management_backend1.model.Room;
import org.example.booking_management_backend1.repository.BookingRepository;
import org.example.booking_management_backend1.repository.CustomerRepository;
import org.example.booking_management_backend1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        // Validate customer exists
        Customer customer = customerRepository.findById(bookingRequest.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + bookingRequest.getCustomerId()));

        // Validate room exists and is available
        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + bookingRequest.getRoomId()));

        if (!room.getAvailable()) {
            throw new IllegalStateException("Room is not available for booking");
        }

        // Check if room is already booked for the requested dates
        boolean isRoomAvailable = roomRepository.findAvailableRooms(
                        bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())
                .stream()
                .anyMatch(r -> r.getId().equals(room.getId()));

        if (!isRoomAvailable) {
            throw new IllegalStateException("Room is not available for the selected dates");
        }

        // Create and save booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());
        booking.setNumberOfGuests(bookingRequest.getNumberOfGuests());
        booking.setStatus("CONFIRMED");

        Booking savedBooking = bookingRepository.save(booking);

        // Update room availability
        room.setAvailable(false);
        roomRepository.save(room);

        return new BookingResponse(savedBooking);
    }

    @Override
    public List<BookingResponse> getBookingsByCustomerId(Long customerId) {
        // Validate customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Customer not found with id: " + customerId);
        }

        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        return bookings.stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        return new BookingResponse(booking);
    }

    @Override
    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // Update booking status
        booking.setStatus("CANCELLED");
        Booking cancelledBooking = bookingRepository.save(booking);

        // Update room availability
        Room room = booking.getRoom();
        room.setAvailable(true);
        roomRepository.save(room);

        return new BookingResponse(cancelledBooking);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList());
    }
}