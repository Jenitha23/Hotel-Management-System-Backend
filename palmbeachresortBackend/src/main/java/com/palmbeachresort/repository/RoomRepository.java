package com.palmbeachresort.repository;

import com.palmbeachresort.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    // Basic CRUD operations
    Optional<Room> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);

    // Availability queries
    List<Room> findByAvailableTrue();
    List<Room> findByRoomTypeAndAvailableTrue(String roomType);
    List<Room> findByPriceBetweenAndAvailableTrue(BigDecimal minPrice, BigDecimal maxPrice);
    List<Room> findByCapacityGreaterThanEqualAndAvailableTrue(Integer capacity);

    // Complex search queries
    @Query("SELECT r FROM Room r WHERE r.available = true AND r.roomType = :roomType AND r.price BETWEEN :minPrice AND :maxPrice")
    List<Room> findAvailableRoomsByTypeAndPriceRange(@Param("roomType") String roomType,
                                                     @Param("minPrice") BigDecimal minPrice,
                                                     @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT DISTINCT r.roomType FROM Room r WHERE r.available = true ORDER BY r.roomType")
    List<String> findDistinctRoomTypes();

    // Sorting queries
    @Query("SELECT r FROM Room r WHERE r.available = true ORDER BY r.price ASC")
    List<Room> findAvailableRoomsOrderByPriceAsc();

    @Query("SELECT r FROM Room r WHERE r.available = true ORDER BY r.price DESC")
    List<Room> findAvailableRoomsOrderByPriceDesc();

    // Statistics queries
    @Query("SELECT COUNT(r) FROM Room r WHERE r.available = true")
    Long countAvailableRooms();

    @Query("SELECT r.roomType, COUNT(r) FROM Room r WHERE r.available = true GROUP BY r.roomType")
    List<Object[]> countAvailableRoomsByType();

    @Query("SELECT MIN(r.price), MAX(r.price), AVG(r.price) FROM Room r WHERE r.available = true")
    Object[] getPriceStatistics();

    // Search with multiple optional parameters
    @Query("SELECT r FROM Room r WHERE r.available = true " +
            "AND (:roomType IS NULL OR r.roomType = :roomType) " +
            "AND (:minPrice IS NULL OR r.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR r.price <= :maxPrice) " +
            "AND (:minCapacity IS NULL OR r.capacity >= :minCapacity) " +
            "ORDER BY r.price ASC")
    List<Room> searchAvailableRooms(@Param("roomType") String roomType,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    @Param("minCapacity") Integer minCapacity);
}