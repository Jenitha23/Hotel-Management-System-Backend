package com.palmbeachresort.service.billing.impl;

import com.palmbeachresort.dto.billing.*;
import com.palmbeachresort.entity.Booking;
import com.palmbeachresort.entity.Room;
import com.palmbeachresort.entity.auth.Customer;
import com.palmbeachresort.entity.menu.FoodOrder;
import com.palmbeachresort.entity.menu.OrderItem;
import com.palmbeachresort.repository.BookingRepository;
import com.palmbeachresort.repository.auth.CustomerRepository;
import com.palmbeachresort.repository.menu.FoodOrderRepository;
import com.palmbeachresort.service.billing.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public BillingResponse getCustomerBill(Long customerId) {
        try {
            System.out.println("🧾 Generating bill for customer: " + customerId);

            // Validate customer exists
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

            // Get active booking for the customer
            Booking activeBooking = getActiveBooking(customerId);

            // Get food orders for the customer
            List<FoodOrder> foodOrders = foodOrderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

            return buildBillingResponse(customer, activeBooking, foodOrders);

        } catch (Exception e) {
            System.err.println("❌ Error generating bill: " + e.getMessage());
            throw new RuntimeException("Failed to generate bill: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BillingResponse getCustomerBillForStay(Long customerId, String bookingReference) {
        try {
            System.out.println("🧾 Generating bill for booking: " + bookingReference + ", customer: " + customerId);

            // Validate customer exists
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

            // Get specific booking
            Booking booking = bookingRepository.findByBookingReference(bookingReference)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with reference: " + bookingReference));

            // Verify customer owns the booking
            if (!booking.getCustomer().getId().equals(customerId)) {
                throw new IllegalArgumentException("Access denied - booking does not belong to customer");
            }

            // Get food orders for this stay period
            List<FoodOrder> foodOrders = getFoodOrdersForStay(customerId, booking);

            return buildBillingResponse(customer, booking, foodOrders);

        } catch (Exception e) {
            System.err.println("❌ Error generating bill for stay: " + e.getMessage());
            throw new RuntimeException("Failed to generate bill: " + e.getMessage());
        }
    }

    private Booking getActiveBooking(Long customerId) {
        // Get the most recent confirmed or checked-in booking
        List<Booking> customerBookings = bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        return customerBookings.stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.CONFIRMED ||
                        booking.getStatus() == Booking.BookingStatus.CHECKED_IN)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active booking found for customer"));
    }

    private List<FoodOrder> getFoodOrdersForStay(Long customerId, Booking booking) {
        // Get food orders that were created during the stay period
        return foodOrderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .filter(order -> !order.getCreatedAt().toLocalDate().isBefore(booking.getCheckInDate()) &&
                        !order.getCreatedAt().toLocalDate().isAfter(booking.getCheckOutDate()))
                .collect(Collectors.toList());
    }

    private BillingResponse buildBillingResponse(Customer customer, Booking booking, List<FoodOrder> foodOrders) {
        // Build room booking summary
        RoomBookingSummary roomSummary = buildRoomBookingSummary(booking);

        // Build food order summaries
        List<FoodOrderSummary> foodSummaries = buildFoodOrderSummaries(foodOrders);

        // Calculate totals
        BigDecimal roomTotal = roomSummary.getTotalAmount();
        BigDecimal foodTotal = calculateFoodTotal(foodSummaries);
        BigDecimal grandTotal = roomTotal.add(foodTotal);

        return new BillingResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                roomSummary,
                foodSummaries,
                roomTotal,
                foodTotal,
                grandTotal
        );
    }

    private RoomBookingSummary buildRoomBookingSummary(Booking booking) {
        Room room = booking.getRoom();
        long numberOfNights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());

        return new RoomBookingSummary(
                booking.getId(),
                booking.getBookingReference(),
                room.getRoomNumber(),
                room.getRoomType(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                (int) numberOfNights,
                room.getPrice(),
                booking.getTotalAmount(),
                booking.getStatus().toString()
        );
    }

    private List<FoodOrderSummary> buildFoodOrderSummaries(List<FoodOrder> foodOrders) {
        return foodOrders.stream()
                .map(this::buildFoodOrderSummary)
                .collect(Collectors.toList());
    }

    private FoodOrderSummary buildFoodOrderSummary(FoodOrder foodOrder) {
        List<FoodItem> foodItems = foodOrder.getItems().stream()
                .map(this::buildFoodItem)
                .collect(Collectors.toList());

        return new FoodOrderSummary(
                foodOrder.getId(),
                foodOrder.getOrderNumber(),
                foodOrder.getCreatedAt(),
                foodOrder.getStatus().toString(),
                foodItems,
                foodOrder.getTotalAmount()
        );
    }

    private FoodItem buildFoodItem(OrderItem orderItem) {
        return new FoodItem(
                orderItem.getMenuItem().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getSubtotal()
        );
    }

    private BigDecimal calculateFoodTotal(List<FoodOrderSummary> foodSummaries) {
        return foodSummaries.stream()
                .map(FoodOrderSummary::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}