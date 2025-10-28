package com.palmbeachresort.controller;

import com.palmbeachresort.dto.CartDTO;
import com.palmbeachresort.dto.CartItemDTO;
import com.palmbeachresort.model.Cart;
import com.palmbeachresort.model.CartItem;
import com.palmbeachresort.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:5176", "http://localhost:5173"})
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("X-Session-Id") String sessionId) {
        try {
            Cart cart = cartService.getCartBySessionId(sessionId);
            return ResponseEntity.ok(convertToDTO(cart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestHeader("X-Session-Id") String sessionId,
            @RequestBody AddToCartRequest request) {
        try {
            if (request.getMenuId() == null || request.getQuantity() == null || request.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Invalid request data");
            }

            Cart cart = cartService.addToCart(sessionId, request.getMenuId(), request.getQuantity());
            return ResponseEntity.ok(convertToDTO(cart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding to cart: " + e.getMessage());
        }
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<?> updateCartItem(
            @RequestHeader("X-Session-Id") String sessionId,
            @PathVariable Long itemId,
            @RequestBody UpdateCartItemRequest request) {
        try {
            Cart cart = cartService.updateCartItem(sessionId, itemId, request.getQuantity());
            return ResponseEntity.ok(convertToDTO(cart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error updating cart item: " + e.getMessage());
        }
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> removeFromCart(
            @RequestHeader("X-Session-Id") String sessionId,
            @PathVariable Long itemId) {
        try {
            Cart cart = cartService.removeFromCart(sessionId, itemId);
            return ResponseEntity.ok(convertToDTO(cart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error removing from cart: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestHeader("X-Session-Id") String sessionId) {
        try {
            cartService.clearCart(sessionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error clearing cart: " + e.getMessage());
        }
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setSessionId(cart.getSessionId());
        dto.setTotalAmount(cart.getTotalAmount());

        if (cart.getItems() != null) {
            dto.setItems(cart.getItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private CartItemDTO convertItemToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        if (item.getMenu() != null) {
            dto.setMenuId(item.getMenu().getId());
            dto.setMenuName(item.getMenu().getName());
            dto.setPrice(item.getMenu().getPrice());
        }
        dto.setQuantity(item.getQuantity());
        if (item.getMenu() != null && item.getQuantity() != null) {
            dto.setItemTotal(item.getMenu().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
        }
        return dto;
    }

    public static class AddToCartRequest {
        private Long menuId;
        private Integer quantity;

        public Long getMenuId() { return menuId; }
        public void setMenuId(Long menuId) { this.menuId = menuId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class UpdateCartItemRequest {
        private Integer quantity;

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}