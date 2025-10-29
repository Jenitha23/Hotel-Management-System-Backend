package com.palmbeachresort.service.impl;

import com.palmbeachresort.dto.RoomResponse;
import com.palmbeachresort.dto.booking.BookingRequest;
import com.palmbeachresort.dto.booking.BookingResponse;
import com.palmbeachresort.dto.booking.BookingUpdateRequest;
import com.palmbeachresort.entity.Booking;
import com.palmbeachresort.entity.Room;
import com.palmbeachresort.entity.auth.Customer;
import com.palmbeachresort.repository.BookingRepository;
import com.palmbeachresort.repository.RoomRepository;
import com.palmbeachresort.repository.auth.CustomerRepository;
import com.palmbeachresort.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest, Long customerId) {
       // Validate customer exists
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        // Validate room exists and is available
        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + bookingRequest.getRoomId()));

        if (!room.getAvailable()) {
            throw new IllegalArgumentException("Room is not available for booking");
        }

        // Validate dates
        validateBookingDates(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        // Check room availability for the requested dates
        if (!isRoomAvailable(room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())) {
            throw new IllegalArgumentException("Room is not available for the selected dates");
        }

        // Validate guest count
        if (bookingRequest.getGuestCount() > room.getCapacity()) {
            throw new IllegalArgumentException("Guest count exceeds room capacity");
        }

        // Calculate total amount
        long numberOfNights = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
        BigDecimal totalAmount = room.getPrice().multiply(BigDecimal.valueOf(numberOfNights));

        // Generate unique booking reference
        String bookingReference = generateBookingReference();

        // Create booking
        Booking booking = new Booking(
                bookingReference,
                customer,
                room,
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                totalAmount,
                bookingRequest.getGuestCount(),
                bookingRequest.getSpecialRequests()
        );

        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getCustomerBookings(Long customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        return bookings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getCustomerBooking(Long bookingId, Long customerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("Access denied - booking does not belong to customer");
        }

        return convertToResponse(booking);
    }

    @Override
    public BookingResponse cancelBooking(Long bookingId, Long customerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("Access denied - booking does not belong to customer");
        }

        // Check if cancellation is allowed (24 hours before check-in)
        if (!canCancelBooking(booking)) {
            throw new IllegalArgumentException("Cancellation is only allowed up to 24 hours before check-in");
        }

        // Check if booking can be cancelled
        if (booking.getStatus() == Booking.BookingStatus.CHECKED_IN ||
                booking.getStatus() == Booking.BookingStatus.CHECKED_OUT) {
            throw new IllegalArgumentException("Cannot cancel booking with status: " + booking.getStatus());
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);
        return convertToResponse(cancelledBooking);
    }

    @Override
    public BookingResponse updateBooking(Long bookingId, Long customerId, BookingUpdateRequest updateRequest) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("Access denied - booking does not belong to customer");
        }

        // Check if booking can be modified
        if (booking.getStatus() != Booking.BookingStatus.PENDING &&
                booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Cannot modify booking with status: " + booking.getStatus());
        }

        // Update fields if provided
        if (updateRequest.getCheckInDate() != null) {
            validateBookingDates(updateRequest.getCheckInDate(),
                    updateRequest.getCheckOutDate() != null ? updateRequest.getCheckOutDate() : booking.getCheckOutDate());
            booking.setCheckInDate(updateRequest.getCheckInDate());
        }

        if (updateRequest.getCheckOutDate() != null) {
            validateBookingDates(updateRequest.getCheckInDate() != null ? updateRequest.getCheckInDate() : booking.getCheckInDate(),
                    updateRequest.getCheckOutDate());
            booking.setCheckOutDate(updateRequest.getCheckOutDate());
        }

        if (updateRequest.getGuestCount() != null) {
            if (updateRequest.getGuestCount() > booking.getRoom().getCapacity()) {
                throw new IllegalArgumentException("Guest count exceeds room capacity");
            }
            booking.setGuestCount(updateRequest.getGuestCount());
        }

        if (updateRequest.getSpecialRequests() != null) {
            booking.setSpecialRequests(updateRequest.getSpecialRequests());
        }

        // Recalculate total amount if dates changed
        if (updateRequest.getCheckInDate() != null || updateRequest.getCheckOutDate() != null) {
            long numberOfNights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
            BigDecimal totalAmount = booking.getRoom().getPrice().multiply(BigDecimal.valueOf(numberOfNights));
            booking.setTotalAmount(totalAmount);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponse(updatedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByStatus(Booking.BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByStatusOrderByCreatedAtDesc(status);
        return bookings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));
        return convertToResponse(booking);
    }

    @Override
    public BookingResponse updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponse(updatedBooking);
    }

    @Override
    public BookingResponse updateBooking(Long bookingId, BookingUpdateRequest updateRequest) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        // Update fields if provided
        if (updateRequest.getCheckInDate() != null) {
            validateBookingDates(updateRequest.getCheckInDate(),
                    updateRequest.getCheckOutDate() != null ? updateRequest.getCheckOutDate() : booking.getCheckOutDate());
            booking.setCheckInDate(updateRequest.getCheckInDate());
        }

        if (updateRequest.getCheckOutDate() != null) {
            validateBookingDates(updateRequest.getCheckInDate() != null ? updateRequest.getCheckInDate() : booking.getCheckInDate(),
                    updateRequest.getCheckOutDate());
            booking.setCheckOutDate(updateRequest.getCheckOutDate());
        }

        if (updateRequest.getGuestCount() != null) {
            if (updateRequest.getGuestCount() > booking.getRoom().getCapacity()) {
                throw new IllegalArgumentException("Guest count exceeds room capacity");
            }
            booking.setGuestCount(updateRequest.getGuestCount());
        }

        if (updateRequest.getSpecialRequests() != null) {
            booking.setSpecialRequests(updateRequest.getSpecialRequests());
        }

        // Recalculate total amount if dates changed
        if (updateRequest.getCheckInDate() != null || updateRequest.getCheckOutDate() != null) {
            long numberOfNights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
            BigDecimal totalAmount = booking.getRoom().getPrice().multiply(BigDecimal.valueOf(numberOfNights));
            booking.setTotalAmount(totalAmount);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponse(updatedBooking);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(roomId, checkInDate, checkOutDate);
        return overlappingBookings.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getTodayCheckIns() {
        List<Booking> checkIns = bookingRepository.findTodayCheckIns(LocalDate.now());
        return checkIns.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getTodayCheckOuts() {
        List<Booking> checkOuts = bookingRepository.findTodayCheckOuts(LocalDate.now());
        return checkOuts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse checkIn(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed bookings can be checked in");
        }

        if (!booking.getCheckInDate().equals(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in is only allowed on the check-in date");
        }

        booking.setStatus(Booking.BookingStatus.CHECKED_IN);
        Booking checkedInBooking = bookingRepository.save(booking);
        return convertToResponse(checkedInBooking);
    }

    @Override
    public BookingResponse checkOut(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != Booking.BookingStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Only checked-in bookings can be checked out");
        }

        booking.setStatus(Booking.BookingStatus.CHECKED_OUT);
        Booking checkedOutBooking = bookingRepository.save(booking);
        return convertToResponse(checkedOutBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getBookingCountByStatus(Booking.BookingStatus status) {
        return bookingRepository.countByStatus(status);
    }

    // Utility methods
    private void validateBookingDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate) || checkInDate.equals(checkOutDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        if (checkInDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
    }

    private String generateBookingReference() {
        return "PBR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean canCancelBooking(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInDateTime = booking.getCheckInDate().atStartOfDay();
        return now.plusHours(24).isBefore(checkInDateTime);
    }

    private BookingResponse convertToResponse(Booking booking) {
        // Convert Room to RoomResponse
        Room room = booking.getRoom();
        RoomResponse roomResponse = new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPrice(),
                room.getCapacity(),
                room.getDescription(),
                room.getAvailable(),
                room.getImageUrl(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );

        // Check if cancellation is allowed
        boolean canCancel = canCancelBooking(booking) &&
                (booking.getStatus() == Booking.BookingStatus.PENDING ||
                        booking.getStatus() == Booking.BookingStatus.CONFIRMED);

        return new BookingResponse(
                booking.getId(),
                booking.getBookingReference(),
                booking.getCustomer().getId(),
                booking.getCustomer().getFullName(),
                booking.getCustomer().getEmail(),
                roomResponse,
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getGuestCount(),
                booking.getSpecialRequests(),
                booking.getCreatedAt(),
                booking.getUpdatedAt(),
                canCancel
        );
    }
}