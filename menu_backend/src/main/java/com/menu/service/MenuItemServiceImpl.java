package com.menu.service;

import com.menu.entity.MenuItem;
import com.menu.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImpl implements com.menu.service.MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;   // Injecting the MenuItem repository

    @Override
    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();   // Fetches all menu items from the database
        // findAll() retrieves all menu items from the menu_items table
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    @Override
    public MenuItem getMenuItemById(int id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    @Override
    public MenuItem updateMenuItem(int id, @RequestBody MenuItem menuItem) {
        Optional<MenuItem> existingMenuItem = menuItemRepository.findById(id);
        if(existingMenuItem.isPresent()) {
            MenuItem updatedItem = existingMenuItem.get();
            updatedItem.setName(menuItem.getName());
            updatedItem.setCategory(menuItem.getCategory());
            updatedItem.setDescription(menuItem.getDescription());
            updatedItem.setPrice(menuItem.getPrice());
            updatedItem.setAvailability(menuItem.getAvailability());
            updatedItem.setImageUrl(menuItem.getImageUrl());
            return menuItemRepository.save(updatedItem);
        }
        return null;
    }

    @Override
    public void deleteMenuItem(int id) {
        menuItemRepository.deleteById(id);
    }

}

