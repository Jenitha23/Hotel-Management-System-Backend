package com.palmbeachresort.controller;

import com.palmbeachresort.model.Menu;
import com.palmbeachresort.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "http://localhost:5176")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenuItems() {
        List<Menu> menuItems = menuRepository.findByAvailableTrue();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Menu>> getMenuByCategory(@PathVariable String category) {
        List<Menu> menuItems = menuRepository.findByCategoryAndAvailableTrue(category);
        return ResponseEntity.ok(menuItems);
    }

    @PostMapping
    public ResponseEntity<Menu> createMenuItem(@RequestBody Menu menu) {
        Menu savedMenu = menuRepository.save(menu);
        return ResponseEntity.ok(savedMenu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenuItem(@PathVariable Long id, @RequestBody Menu menu) {
        if (!menuRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        menu.setId(id);
        Menu updatedMenu = menuRepository.save(menu);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        if (!menuRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        menuRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}