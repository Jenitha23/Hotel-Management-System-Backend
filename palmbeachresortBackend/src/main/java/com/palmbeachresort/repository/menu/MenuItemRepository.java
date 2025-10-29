// [file name]: MenuItemRepository.java
package com.palmbeachresort.repository.menu;

import com.palmbeachresort.entity.menu.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long>, JpaSpecificationExecutor<MenuItem> {

    List<MenuItem> findByAvailableTrue();
    List<MenuItem> findByCategoryAndAvailableTrue(String category);
    List<MenuItem> findByPriceBetweenAndAvailableTrue(BigDecimal minPrice, BigDecimal maxPrice);
    Optional<MenuItem> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT DISTINCT m.category FROM MenuItem m WHERE m.available = true ORDER BY m.category")
    List<String> findDistinctCategories();

    @Query("SELECT m FROM MenuItem m WHERE m.available = true AND " +
            "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:category IS NULL OR m.category = :category) AND " +
            "(:minPrice IS NULL OR m.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR m.price <= :maxPrice)")
    List<MenuItem> searchAvailableItems(@Param("name") String name,
                                        @Param("category") String category,
                                        @Param("minPrice") BigDecimal minPrice,
                                        @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT COUNT(m) FROM MenuItem m WHERE m.available = true")
    Long countAvailableItems();
}