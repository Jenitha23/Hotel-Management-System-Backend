package com.hotelmanagement.project.service;

import com.hotelmanagement.project.dto.OrderRequest;
import com.hotelmanagement.project.model.Order;
import com.hotelmanagement.project.model.OrderStatus;
import com.hotelmanagement.project.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repo;
    public OrderService(OrderRepository repo) { this.repo = repo; }

    public List<Order> list(OrderStatus status, String room, String q) {
        return repo.search(status, room, q);
    }

    public Order get(Long id) { return repo.findById(id).orElseThrow(); }

    @Transactional
    public Order create(OrderRequest r) {
        Order o = new Order();
        apply(o, r);
        if (o.getOrderTime() == null) o.setOrderTime(LocalDateTime.now());
        return repo.save(o);
    }

    @Transactional
    public Order update(Long id, OrderRequest r) {
        Order o = get(id);
        apply(o, r);
        return repo.save(o);
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order o = get(id);
        o.setStatus(status);
        return repo.save(o);
    }

    public void delete(Long id) { repo.deleteById(id); }

    private void apply(Order o, OrderRequest r) {
        if (r.customerName != null) o.setCustomerName(r.customerName);
        if (r.roomNumber != null) o.setRoomNumber(r.roomNumber);
        if (r.item != null) o.setItem(r.item);
        if (r.quantity > 0) o.setQuantity(r.quantity);
        if (r.orderTime != null) o.setOrderTime(r.orderTime);
        if (r.estimatedTime != null) o.setEstimatedTime(r.estimatedTime);
        if (r.status != null) o.setStatus(r.status);
        if (r.location != null) o.setLocation(r.location);
        if (r.specialInstructions != null) o.setSpecialInstructions(r.specialInstructions);
        if (r.priority != null) o.setPriority(r.priority);
    }
}
