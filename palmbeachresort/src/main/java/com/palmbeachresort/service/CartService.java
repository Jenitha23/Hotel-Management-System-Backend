package com.palmbeachresort.service;

import com.palmbeachresort.model.Cart;
import com.palmbeachresort.model.CartItem;
import com.palmbeachresort.model.Menu;
import com.palmbeachresort.repository.CartRepository;
import com.palmbeachresort.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuRepository menuRepository;

    public Cart getOrCreateCart(String sessionId) {
        Optional<Cart> existingCart = cartRepository.findBySessionId(sessionId);
        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Cart newCart = new Cart(sessionId);
            return cartRepository.save(newCart);
        }
    }

    public Cart addToCart(String sessionId, Long menuId, Integer quantity) {
        try {
            Cart cart = getOrCreateCart(sessionId);
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + menuId));

            // Check if item already exists in cart
            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getMenu().getId().equals(menuId))
                    .findFirst();

            if (existingItem.isPresent()) {
                // Update quantity
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                // Add new item
                CartItem newItem = new CartItem(menu, quantity);
                cart.getItems().add(newItem);
            }

            // Recalculate total
            cart.calculateTotal();
            return cartRepository.save(cart);

        } catch (Exception e) {
            throw new RuntimeException("Error adding to cart: " + e.getMessage(), e);
        }
    }

    public Cart updateCartItem(String sessionId, Long itemId, Integer quantity) {
        Cart cart = getCartBySessionId(sessionId);

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + itemId));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        cart.calculateTotal();
        return cartRepository.save(cart);
    }

    public Cart removeFromCart(String sessionId, Long itemId) {
        Cart cart = getCartBySessionId(sessionId);

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));
        if (removed) {
            cart.calculateTotal();
            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("Cart item not found with id: " + itemId);
        }
    }

    public Cart getCartBySessionId(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cart not found for session: " + sessionId));
    }

    public void clearCart(String sessionId) {
        Cart cart = getCartBySessionId(sessionId);
        cart.getItems().clear();
        cart.setTotalAmount(java.math.BigDecimal.ZERO);
        cartRepository.save(cart);
    }
}