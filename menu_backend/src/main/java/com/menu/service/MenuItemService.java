package com.menu.service;

import com.menu.entity.MenuItem;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MenuItemService {
    // Method to save a menu item
    public MenuItem saveMenuItem(MenuItem menuItem);

    // Method to retrieve all menu items
    public List<MenuItem> getAllMenuItems();  // Ensure this matches the implementation

    // Optional: You can add more methods like finding by name, category, etc.
    public List<MenuItem> getMenuItemsByCategory(String category);

    //  Retrieve a menu item by ID
    public MenuItem getMenuItemById(int id);

    //  Update a menu item
    public MenuItem updateMenuItem(int id, @RequestBody MenuItem menuItem);

    //  Delete a menu item
    public void deleteMenuItem(int id);
}
