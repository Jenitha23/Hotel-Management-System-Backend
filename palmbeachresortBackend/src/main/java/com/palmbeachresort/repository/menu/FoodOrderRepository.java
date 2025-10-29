// [file name]: FoodOrderRepository.java
package com.palmbeachresort.repository.menu;

import com.palmbeachresort.entity.menu.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<FoodOrder> findByStatusOrderByCreatedAtDesc(FoodOrder.OrderStatus status);
    Optional<FoodOrder> findByOrderNumber(String orderNumber);


    @Query("SELECT fo FROM FoodOrder fo JOIN FETCH fo.items i JOIN FETCH i.menuItem WHERE fo.customer.id = :customerId ORDER BY fo.createdAt DESC")
    List<FoodOrder> findByCustomerIdWithItems(@Param("customerId") Long customerId);

    @Query("SELECT fo FROM FoodOrder fo JOIN FETCH fo.items i JOIN FETCH i.menuItem WHERE fo.id = :id")
    Optional<FoodOrder> findByIdWithItems(@Param("id") Long id);

    @Query("SELECT COUNT(fo) FROM FoodOrder fo WHERE fo.status = :status")
    Long countByStatus(@Param("status") FoodOrder.OrderStatus status);

    @Query("SELECT fo FROM FoodOrder fo WHERE fo.createdAt BETWEEN :startDate AND :endDate ORDER BY fo.createdAt DESC")
    List<FoodOrder> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT fo FROM FoodOrder fo WHERE fo.status IN ('PENDING', 'CONFIRMED', 'PREPARING') ORDER BY fo.createdAt ASC")
    List<FoodOrder> findActiveKitchenOrders();


}