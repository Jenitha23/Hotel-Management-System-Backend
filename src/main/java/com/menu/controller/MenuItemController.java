package com.menu.controller;


import com.menu.entity.MenuItem;
import com.menu.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/add")
    public String addMenuItem(@RequestBody MenuItem menuItem) {
        menuItemService.saveMenuItem(menuItem);
        return "New Menu Item added Successfully";
    }

    @GetMapping("/getAll")
    public List<MenuItem> getAllMenuItems() {
        return menuItemService.getAllMenuItems();
    }

    // Get menu items by category
    @GetMapping("/category/{category}")
    public List<MenuItem> getMenuItemsByCategory(@PathVariable("category") String category) {
        return menuItemService.getMenuItemsByCategory(category);
    }

    @PutMapping("/update/{id}")
    public String updateMenuItem(@PathVariable("id") int id, @RequestBody MenuItem menuItem) {
        MenuItem existingItem = menuItemService.getMenuItemById(id);
        if(existingItem != null) {
            existingItem.setName(menuItem.getName());
            existingItem.setDescription(menuItem.getDescription());
            existingItem.setPrice(menuItem.getPrice());
            existingItem.setCategory(menuItem.getCategory());
            existingItem.setAvailability(menuItem.getAvailability());
            existingItem.setImageUrl(menuItem.getImageUrl());

            menuItemService.saveMenuItem(existingItem);
            return "Menu Item updated Successfully";
        }
        return "Menu Item not found";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable("id") int id) {
        menuItemService.deleteMenuItem(id);
        return "Menu Item deleted Successfully";
    }


}
