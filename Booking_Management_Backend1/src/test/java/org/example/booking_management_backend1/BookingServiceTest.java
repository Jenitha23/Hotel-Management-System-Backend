package org.example.booking_management_backend1;


import org.example.booking_management_backend1.dto.BookingRequest;
import org.example.booking_management_backend1.model.Booking;
import org.example.booking_management_backend1.model.Customer;
import org.example.booking_management_backend1.model.Room;
import org.example.booking_management_backend1.repository.BookingRepository;
import org.example.booking_management_backend1.repository.CustomerRepository;
import org.example.booking_management_backend1.repository.RoomRepository;
import org.example.booking_management_backend1.service.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Customer testCustomer;
    private Room testRoom;
    private BookingRequest testBookingRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setPhone("123-456-7890");

        testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomNumber("101");
        testRoom.setType("Standard");
        testRoom.setPrice(100.00);
        testRoom.setAvailable(true);

        testBookingRequest = new BookingRequest();
        testBookingRequest.setCustomerId(1L);
        testBookingRequest.setRoomId(1L);
        testBookingRequest.setCheckInDate(LocalDate.now().plusDays(1));
        testBookingRequest.setCheckOutDate(LocalDate.now().plusDays(3));
        testBookingRequest.setNumberOfGuests(2);
    }

    @Test
    void createBooking_ShouldReturnBookingResponse_WhenValidRequest() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));
        when(roomRepository.findAvailableRooms(any(), any())).thenReturn(java.util.Arrays.asList(testRoom));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(1L);
            return booking;
        });

        var result = bookingService.createBooking(testBookingRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
}
