// [file name]: MenuServiceImpl.java
package com.palmbeachresort.service.menu.impl;

import com.palmbeachresort.dto.menu.MenuItemRequest;
import com.palmbeachresort.dto.menu.MenuItemResponse;
import com.palmbeachresort.entity.menu.MenuItem;
import com.palmbeachresort.repository.menu.MenuItemRepository;
import com.palmbeachresort.service.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest) {
        // Check if menu item name already exists
        if (menuItemRepository.existsByName(menuItemRequest.getName())) {
            throw new IllegalArgumentException("Menu item with name '" + menuItemRequest.getName() + "' already exists");
        }

        MenuItem menuItem = new MenuItem(
                menuItemRequest.getName(),
                menuItemRequest.getDescription(),
                menuItemRequest.getPrice(),
                menuItemRequest.getCategory(),
                menuItemRequest.getImageUrl(),
                menuItemRequest.getAvailable(),
                menuItemRequest.getPreparationTime()
        );

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return convertToResponse(savedMenuItem);
    }

    @Override
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest menuItemRequest) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));

        // Check if name is being changed and if it already exists
        if (!existingMenuItem.getName().equals(menuItemRequest.getName()) &&
                menuItemRepository.existsByName(menuItemRequest.getName())) {
            throw new IllegalArgumentException("Menu item with name '" + menuItemRequest.getName() + "' already exists");
        }

        // Update all fields
        existingMenuItem.setName(menuItemRequest.getName());
        existingMenuItem.setDescription(menuItemRequest.getDescription());
        existingMenuItem.setPrice(menuItemRequest.getPrice());
        existingMenuItem.setCategory(menuItemRequest.getCategory());
        existingMenuItem.setImageUrl(menuItemRequest.getImageUrl());
        existingMenuItem.setAvailable(menuItemRequest.getAvailable());
        existingMenuItem.setPreparationTime(menuItemRequest.getPreparationTime());

        MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
        return convertToResponse(updatedMenuItem);
    }

    @Override
    public MenuItemResponse updateMenuItemPartial(Long id, Map<String, Object> updates) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));

        // Handle each field update
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    String newName = (String) value;
                    if (!existingMenuItem.getName().equals(newName) &&
                            menuItemRepository.existsByName(newName)) {
                        throw new IllegalArgumentException("Menu item with name '" + newName + "' already exists");
                    }
                    existingMenuItem.setName(newName);
                    break;
                case "description":
                    existingMenuItem.setDescription((String) value);
                    break;
                case "price":
                    existingMenuItem.setPrice(new BigDecimal(value.toString()));
                    break;
                case "category":
                    existingMenuItem.setCategory((String) value);
                    break;
                case "imageUrl":
                    existingMenuItem.setImageUrl((String) value);
                    break;
                case "available":
                    existingMenuItem.setAvailable((Boolean) value);
                    break;
                case "preparationTime":
                    existingMenuItem.setPreparationTime((Integer) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
        return convertToResponse(updatedMenuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new IllegalArgumentException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAvailableMenuItems() {
        return menuItemRepository.findByAvailableTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategoryAndAvailableTrue(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return menuItemRepository.findByPriceBetweenAndAvailableTrue(minPrice, maxPrice).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> searchMenuItems(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        List<MenuItem> menuItems = menuItemRepository.searchAvailableItems(name, category, minPrice, maxPrice);
        return menuItems.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAvailableCategories() {
        return menuItemRepository.findDistinctCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));
        return convertToResponse(menuItem);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return menuItemRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemByName(String name) {
        MenuItem menuItem = menuItemRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with name: " + name));
        return convertToResponse(menuItem);
    }

    private MenuItemResponse convertToResponse(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getCategory(),
                menuItem.getImageUrl(),
                menuItem.getAvailable(),
                menuItem.getPreparationTime(),
                menuItem.getCreatedAt(),
                menuItem.getUpdatedAt()
        );
    }
}