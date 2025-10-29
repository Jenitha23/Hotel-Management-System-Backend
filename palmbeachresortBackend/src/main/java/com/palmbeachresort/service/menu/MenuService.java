// [file name]: MenuService.java
package com.palmbeachresort.service.menu;

import com.palmbeachresort.dto.menu.MenuItemRequest;
import com.palmbeachresort.dto.menu.MenuItemResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MenuService {

    // Admin operations
    MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest);
    MenuItemResponse updateMenuItem(Long id, MenuItemRequest menuItemRequest);
    MenuItemResponse updateMenuItemPartial(Long id, Map<String, Object> updates);
    void deleteMenuItem(Long id);
    List<MenuItemResponse> getAllMenuItems();

    // Customer operations
    List<MenuItemResponse> getAvailableMenuItems();
    List<MenuItemResponse> getMenuItemsByCategory(String category);
    List<MenuItemResponse> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<MenuItemResponse> searchMenuItems(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);
    List<String> getAvailableCategories();
    MenuItemResponse getMenuItemById(Long id);

    // Utility methods
    boolean existsByName(String name);
    MenuItemResponse getMenuItemByName(String name);
}