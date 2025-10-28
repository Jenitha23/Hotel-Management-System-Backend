package org.example.booking_management_backend1;


import org.example.booking_management_backend1.controller.BookingController;
import org.example.booking_management_backend1.dto.BookingRequest;
import org.example.booking_management_backend1.dto.BookingResponse;
import org.example.booking_management_backend1.model.Booking;
import org.example.booking_management_backend1.model.Customer;
import org.example.booking_management_backend1.model.Room;
import org.example.booking_management_backend1.service.BookingService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Test
    void createBooking_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Create a mock Booking object with all required nested objects
        Booking mockBooking = new Booking();
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setName("Test Customer");
        
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setRoomNumber("101");
        
        mockBooking.setId(1L);
        mockBooking.setCustomer(mockCustomer);
        mockBooking.setRoom(mockRoom);
        mockBooking.setCheckInDate(LocalDate.of(2023, 12, 15));
        mockBooking.setCheckOutDate(LocalDate.of(2023, 12, 17));
        mockBooking.setNumberOfGuests(2);
        mockBooking.setStatus("CONFIRMED");
        
        BookingResponse mockResponse = new BookingResponse(mockBooking);
        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(mockResponse);

        String bookingRequestJson = """
        {
            "customerId": 1,
            "roomId": 1,
            "checkInDate": "2023-12-15",
            "checkOutDate": "2023-12-17",
            "numberOfGuests": 2
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
