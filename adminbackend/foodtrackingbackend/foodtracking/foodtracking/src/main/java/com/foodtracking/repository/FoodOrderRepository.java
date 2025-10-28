package com.foodtracking.repository;

import com.foodtracking.model.FoodOrder;
import com.foodtracking.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, String> {
    List<FoodOrder> findByStatus(OrderStatus status);
    List<FoodOrder> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT COUNT(f) FROM FoodOrder f WHERE f.status = :status")
    long countByStatus(OrderStatus status);
}