package com.palmbeach.menu.repository;

import com.palmbeach.menu.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Page<MenuItem> findByDeletedFalse(Pageable pageable);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.deleted = false AND " +
            "(:q IS NULL OR LOWER(mi.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(mi.description) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
            "(:categoryId IS NULL OR mi.category.id = :categoryId) AND " +
            "(:available IS NULL OR mi.available = :available) AND " +
            "(:minPrice IS NULL OR mi.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR mi.price <= :maxPrice)")
    Page<MenuItem> search(
            @Param("q") String searchTerm,
            @Param("categoryId") Long categoryId,
            @Param("available") Boolean available,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}