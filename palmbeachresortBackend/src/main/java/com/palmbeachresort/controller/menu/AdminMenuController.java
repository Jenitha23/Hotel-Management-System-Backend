// [file name]: AdminMenuController.java
package com.palmbeachresort.controller.menu;

import com.palmbeachresort.dto.menu.MenuItemRequest;
import com.palmbeachresort.dto.menu.MenuItemResponse;
import com.palmbeachresort.service.menu.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/menu")
@CrossOrigin(origins = {"https://frontend-palmbeachresort.vercel.app", "http://localhost:3000"},
        allowCredentials = "true")
public class AdminMenuController {

    @Autowired
    private MenuService menuService;

    /**
     * Create a new menu item
     * POST /api/admin/menu
     */
    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(@Valid @RequestBody MenuItemRequest menuItemRequest) {
        MenuItemResponse createdItem = menuService.createMenuItem(menuItemRequest);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Update entire menu item
     * PUT /api/admin/menu/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(@PathVariable Long id,
                                                           @Valid @RequestBody MenuItemRequest menuItemRequest) {
        MenuItemResponse updatedItem = menuService.updateMenuItem(id, menuItemRequest);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Partial update of menu item
     * PATCH /api/admin/menu/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItemPartial(@PathVariable Long id,
                                                                  @RequestBody Map<String, Object> updates) {
        MenuItemResponse updatedItem = menuService.updateMenuItemPartial(id, updates);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Delete a menu item
     * DELETE /api/admin/menu/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all menu items (including unavailable ones - Admin only)
     * GET /api/admin/menu
     */
    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getAllMenuItems() {
        List<MenuItemResponse> menuItems = menuService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Get menu item by ID (Admin view - can see unavailable items)
     * GET /api/admin/menu/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) {
        MenuItemResponse menuItem = menuService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    /**
     * Check if menu item name exists
     * GET /api/admin/menu/check/{name}
     */
    @GetMapping("/check/{name}")
    public ResponseEntity<Boolean> checkMenuItemNameExists(@PathVariable String name) {
        boolean exists = menuService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}