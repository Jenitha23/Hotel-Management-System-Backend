package com.palmbeach.menu.service;

import com.palmbeach.menu.dto.MenuItemDTO;
import com.palmbeach.menu.entity.Category;
import com.palmbeach.menu.entity.MenuItem;
import com.palmbeach.menu.repository.CategoryRepository;
import com.palmbeach.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public Page<MenuItem> searchMenuItems(String q, Long categoryId, Boolean available,
                                          BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return menuItemRepository.search(q, categoryId, available, minPrice, maxPrice, pageable);
    }

    public MenuItem findById(Long id) {
        return menuItemRepository.findById(id)
                .filter(item -> !item.getDeleted())
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
    }

    @Transactional
    public MenuItem create(MenuItemDTO menuItemDTO) {
        Category category = categoryRepository.findById(menuItemDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + menuItemDTO.getCategoryId()));

        MenuItem menuItem = new MenuItem();
        mapDtoToEntity(menuItemDTO, menuItem, category);

        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public MenuItem update(Long id, MenuItemDTO menuItemDTO) {
        MenuItem existingItem = findById(id);
        Category category = categoryRepository.findById(menuItemDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + menuItemDTO.getCategoryId()));

        mapDtoToEntity(menuItemDTO, existingItem, category);

        return menuItemRepository.save(existingItem);
    }

    @Transactional
    public void delete(Long id) {
        MenuItem menuItem = findById(id);
        menuItem.setDeleted(true);
        menuItemRepository.save(menuItem);
    }

    private void mapDtoToEntity(MenuItemDTO dto, MenuItem entity, Category category) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());
        entity.setAvailable(dto.getAvailable());
        entity.setCategory(category);
    }
}