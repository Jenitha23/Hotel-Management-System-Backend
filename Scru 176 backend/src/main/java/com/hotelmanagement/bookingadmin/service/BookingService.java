package com.hotelmanagement.bookingadmin.service;


import com.hotelmanagement.bookingadmin.api.requests.UpdateBookingRequest;
import com.hotelmanagement.bookingadmin.api.requests.UpdateStatusRequest;
import com.hotelmanagement.bookingadmin.domain.*;
import com.hotelmanagement.bookingadmin.dto.*;
import com.hotelmanagement.bookingadmin.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.hotelmanagement.bookingadmin.service.BookingSpecifications.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final RoomRepository roomRepo;

    public BookingService(BookingRepository bookingRepo, RoomRepository roomRepo) {
        this.bookingRepo = bookingRepo;
        this.roomRepo = roomRepo;
    }

    @Transactional(readOnly = true)
    public Page<BookingSummaryDto> getAll(
            BookingStatus status,
            String roomType,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    ) {
        Specification<Booking> spec = Specification.allOf(
                hasStatus(status),
                hasRoomType(roomType),
                intersectsDateRange(from, to)
        );



        return bookingRepo.findAll(spec, pageable)
                .map(this::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public BookingDetailDto getById(Long id) {
        Booking b = bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));
        return toDetailDto(b);
    }

    @Transactional
    public BookingDetailDto updateStatus(Long id, UpdateStatusRequest req) {
        Booking b = bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));

        // optimistic lock check
        if (!b.getVersion().equals(req.version())) {
            throw new BadRequestException("Version conflict: stale data. Reload and try again.");
        }

        // basic allowed transitions (you can tighten if needed)
        if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("Cannot change status from %s".formatted(b.getStatus()));
        }

        b.setStatus(req.status());
        // JPA @Version auto-increments on flush
        return toDetailDto(b);
    }

    @Transactional
    public BookingDetailDto updateBooking(Long id, UpdateBookingRequest req) {
        Booking b = bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));

        if (!b.getVersion().equals(req.version())) {
            throw new BadRequestException("Version conflict: stale data. Reload and try again.");
        }

        Room room = roomRepo.findById(req.roomId())
                .orElseThrow(() -> new NotFoundException("Room %d not found".formatted(req.roomId())));

        if (!req.checkInDate().isBefore(req.checkOutDate())) {
            throw new BadRequestException("checkInDate must be before checkOutDate");
        }

        // (Optional) add availability check here…

        b.setRoom(room);
        b.setCheckInDate(req.checkInDate());
        b.setCheckOutDate(req.checkOutDate());
        b.setSpecialRequest(req.specialRequest());

        return toDetailDto(b);
    }

    @Transactional
    public void delete(Long id) {
        Booking b = bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));
        bookingRepo.delete(b);
    }

    /* ---------- mapping ---------- */

    private BookingSummaryDto toSummaryDto(Booking b) {
        return new BookingSummaryDto(
                b.getId(),
                b.getCustomer().getFullName(),
                b.getCustomer().getEmail(),
                b.getRoom().getRoomNumber(),
                b.getRoom().getRoomType(),
                b.getBookingDate(),
                b.getCheckInDate(),
                b.getCheckOutDate(),
                b.getStatus()
        );
    }

    private BookingDetailDto toDetailDto(Booking b) {
        return new BookingDetailDto(
                b.getId(),
                b.getCustomer().getId(),
                b.getCustomer().getFullName(),
                b.getCustomer().getEmail(),
                b.getRoom().getId(),
                b.getRoom().getRoomNumber(),
                b.getRoom().getRoomType(),
                b.getBookingDate(),
                b.getCheckInDate(),
                b.getCheckOutDate(),
                b.getStatus(),
                b.getSpecialRequest(),
                b.getVersion()
        );
    }
}

