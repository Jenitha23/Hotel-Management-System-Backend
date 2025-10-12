package com.palmbeachresort.config;

import com.palmbeachresort.model.Order;
import com.palmbeachresort.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only insert data if database is empty
        if (orderRepository.count() == 0) {
            System.out.println("Loading sample data...");

            Order order1 = new Order();
            order1.setId("ORD-001");
            order1.setCustomerName("Sarah Johnson");
            order1.setRoomNumber("247");
            order1.setItem("Sunset Dinner Special");
            order1.setQuantity(1);
            order1.setOrderTime("6:30 PM");
            order1.setEstimatedTime("7:15 PM");
            order1.setStatus(Order.OrderStatus.PREPARING);
            order1.setLocation("Beachside Restaurant");
            order1.setSpecialInstructions("No shellfish");
            order1.setPriority(Order.Priority.NORMAL);

            Order order2 = new Order();
            order2.setId("ORD-002");
            order2.setCustomerName("Sarah Johnson");
            order2.setRoomNumber("247");
            order2.setItem("Beach Bar & Appetizers");
            order2.setQuantity(1);
            order2.setOrderTime("5:45 PM");
            order2.setEstimatedTime("6:00 PM");
            order2.setStatus(Order.OrderStatus.READY_TO_DELIVER);
            order2.setLocation("Pool Bar");
            order2.setPriority(Order.Priority.NORMAL);

            Order order3 = new Order();
            order3.setId("ORD-003");
            order3.setCustomerName("Mike Chen");
            order3.setRoomNumber("156");
            order3.setItem("Tropical Breakfast Buffet");
            order3.setQuantity(2);
            order3.setOrderTime("8:00 AM");
            order3.setEstimatedTime("8:30 AM");
            order3.setStatus(Order.OrderStatus.DELIVERED);
            order3.setLocation("Main Restaurant");
            order3.setPriority(Order.Priority.NORMAL);

            Order order4 = new Order();
            order4.setId("ORD-004");
            order4.setCustomerName("Emily Davis");
            order4.setRoomNumber("312");
            order4.setItem("Poolside Lunch & Cocktails");
            order4.setQuantity(1);
            order4.setOrderTime("12:30 PM");
            order4.setEstimatedTime("1:00 PM");
            order4.setStatus(Order.OrderStatus.DONE);
            order4.setLocation("Pool Grill");
            order4.setSpecialInstructions("Extra spicy");
            order4.setPriority(Order.Priority.HIGH);

            Order order5 = new Order();
            order5.setId("ORD-005");
            order5.setCustomerName("Robert Wilson");
            order5.setRoomNumber("089");
            order5.setItem("Room Service Breakfast");
            order5.setQuantity(1);
            order5.setOrderTime("7:15 AM");
            order5.setEstimatedTime("8:00 AM");
            order5.setStatus(Order.OrderStatus.ORDERED);
            order5.setLocation("Room Service");
            order5.setPriority(Order.Priority.URGENT);

            orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5));
            System.out.println("Sample data loaded successfully! " + orderRepository.count() + " orders created.");
        } else {
            System.out.println("Database already contains " + orderRepository.count() + " orders. Skipping data initialization.");
        }
    }
}