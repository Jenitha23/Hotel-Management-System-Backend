// [file name]: CustomerCartController.java
package com.palmbeachresort.controller.menu;

import com.palmbeachresort.dto.menu.CartItemRequest;
import com.palmbeachresort.dto.menu.CartResponse;
import com.palmbeachresort.service.menu.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/cart")
@CrossOrigin(origins = {"https://frontend-palmbeachresort.vercel.app", "http://localhost:3000"},
        allowCredentials = "true")
public class CustomerCartController {

    @Autowired
    private CartService cartService;

    /**
     * Get customer's cart
     * GET /api/customer/cart
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Add item to cart
     * POST /api/customer/cart/items
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest,
                                                      HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartResponse cart = cartService.addItemToCart(userId, cartItemRequest);
        return ResponseEntity.ok(cart);
    }

    /**
     * Update cart item quantity
     * PUT /api/customer/cart/items/{cartItemId}
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable Long cartItemId,
                                                       @RequestParam Integer quantity,
                                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartResponse cart = cartService.updateCartItem(userId, cartItemId, quantity);
        return ResponseEntity.ok(cart);
    }

    /**
     * Remove item from cart
     * DELETE /api/customer/cart/items/{cartItemId}
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItemFromCart(@PathVariable Long cartItemId,
                                                           HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartResponse cart = cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Clear entire cart
     * DELETE /api/customer/cart
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get customer's cart with detailed items
     * GET /api/customer/cart/details
     */
    @GetMapping("/details")
    public ResponseEntity<CartResponse> getCartWithDetails(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartResponse cart = cartService.getCartWithDetails(userId);
        return ResponseEntity.ok(cart);
    }
}