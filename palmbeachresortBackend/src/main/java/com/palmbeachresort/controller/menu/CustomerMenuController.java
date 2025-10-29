// [file name]: CustomerMenuController.java
package com.palmbeachresort.controller.menu;

import com.palmbeachresort.dto.menu.MenuItemResponse;
import com.palmbeachresort.service.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/customer/menu")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerMenuController {

    @Autowired
    private MenuService menuService;

    /**
     * Get all available menu items
     * GET /api/customer/menu
     */
    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getAvailableMenuItems() {
        List<MenuItemResponse> menuItems = menuService.getAvailableMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Get available menu item by ID
     * GET /api/customer/menu/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getAvailableMenuItemById(@PathVariable Long id) {
        MenuItemResponse menuItem = menuService.getMenuItemById(id);
        if (!menuItem.getAvailable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItem);
    }

    /**
     * Get menu items by category (only available)
     * GET /api/customer/menu/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItemResponse> menuItems = menuService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Get menu items by price range (only available)
     * GET /api/customer/menu/price-range?minPrice=10&maxPrice=50
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<MenuItemResponse> menuItems = menuService.getMenuItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Advanced search with multiple filters
     * GET /api/customer/menu/search?name=pizza&category=MAIN&minPrice=10&maxPrice=30
     */
    @GetMapping("/search")
    public ResponseEntity<List<MenuItemResponse>> searchMenuItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<MenuItemResponse> menuItems = menuService.searchMenuItems(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Get all available categories
     * GET /api/customer/menu/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAvailableCategories() {
        List<String> categories = menuService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }
}