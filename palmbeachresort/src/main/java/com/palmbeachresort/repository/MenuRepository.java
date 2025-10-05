package com.palmbeachresort.repository;

import com.palmbeachresort.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByAvailableTrue();
    List<Menu> findByCategoryAndAvailableTrue(String category);
}