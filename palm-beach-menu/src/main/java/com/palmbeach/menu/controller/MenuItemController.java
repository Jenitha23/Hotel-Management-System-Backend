package com.palmbeach.menu.controller;

import com.palmbeach.menu.dto.MenuItemDTO;
import com.palmbeach.menu.entity.MenuItem;
import com.palmbeach.menu.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5178")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping
    public Page<MenuItemDTO> getMenuItems(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItem> menuItems = menuItemService.searchMenuItems(q, categoryId, available, minPrice, maxPrice, pageable);

        return menuItems.map(this::convertToDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable Long id) {
        MenuItem menuItem = menuItemService.findById(id);
        return ResponseEntity.ok(convertToDTO(menuItem));
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        MenuItem createdItem = menuItemService.create(menuItemDTO);
        return ResponseEntity.ok(convertToDTO(createdItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDTO menuItemDTO) {
        MenuItem updatedItem = menuItemService.update(id, menuItemDTO);
        return ResponseEntity.ok(convertToDTO(updatedItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.delete(id);
        return ResponseEntity.ok().build();
    }

    private MenuItemDTO convertToDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setImageUrl(menuItem.getImageUrl());
        dto.setAvailable(menuItem.getAvailable());

        if (menuItem.getCategory() != null) {
            dto.setCategoryId(menuItem.getCategory().getId());
            dto.setCategoryName(menuItem.getCategory().getName());
        }

        return dto;
    }
}