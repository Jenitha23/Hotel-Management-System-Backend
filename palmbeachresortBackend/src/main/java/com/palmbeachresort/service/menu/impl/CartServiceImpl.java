// [file name]: CartServiceImpl.java
package com.palmbeachresort.service.menu.impl;

import com.palmbeachresort.dto.menu.CartItemRequest;
import com.palmbeachresort.dto.menu.CartItemResponse;
import com.palmbeachresort.dto.menu.CartResponse;
import com.palmbeachresort.entity.menu.Cart;
import com.palmbeachresort.entity.menu.CartItem;
import com.palmbeachresort.entity.menu.MenuItem;
import com.palmbeachresort.entity.auth.User;
import com.palmbeachresort.repository.menu.CartRepository;
import com.palmbeachresort.repository.menu.MenuItemRepository;
import com.palmbeachresort.repository.auth.UserRepository;
import com.palmbeachresort.service.menu.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CartResponse getCart(Long userId) {
        try {
            System.out.println("🛒 Getting cart for user: " + userId);
            Cart cart = getOrCreateCart(userId);
            return convertToResponse(cart);
        } catch (Exception e) {
            System.err.println("❌ Error getting cart: " + e.getMessage());
            return createEmptyCartResponse(userId);
        }
    }

    @Override
    public CartResponse addItemToCart(Long userId, CartItemRequest cartItemRequest) {
        try {
            System.out.println("➕ Adding item to cart for user: " + userId + ", item: " + cartItemRequest.getMenuItemId());

            Cart cart = getOrCreateCart(userId);

            // Validate menu item
            MenuItem menuItem = menuItemRepository.findById(cartItemRequest.getMenuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + cartItemRequest.getMenuItemId()));

            if (!menuItem.getAvailable()) {
                throw new IllegalArgumentException("Menu item is not available");
            }

            // Ensure items list is not null
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }

            // Check if item already exists in cart
            CartItem existingItem = cart.getItems().stream()
                    .filter(item -> item != null && item.getMenuItem().getId().equals(cartItemRequest.getMenuItemId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                // Update quantity if item exists
                existingItem.setQuantity(existingItem.getQuantity() + cartItemRequest.getQuantity());
                System.out.println("📈 Updated quantity for existing item: " + existingItem.getMenuItem().getName());
            } else {
                // Add new item to cart
                CartItem newItem = new CartItem(cart, menuItem, cartItemRequest.getQuantity());
                cart.addItem(newItem);
                System.out.println("🆕 Added new item: " + menuItem.getName());
            }

            Cart updatedCart = cartRepository.save(cart);
            System.out.println("✅ Item added to cart successfully");
            return convertToResponse(updatedCart);

        } catch (Exception e) {
            System.err.println("❌ Error adding item to cart: " + e.getMessage());
            throw new RuntimeException("Failed to add item to cart: " + e.getMessage());
        }
    }

    @Override
    public CartResponse updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        try {
            System.out.println("✏️ Updating cart item: " + cartItemId + " for user: " + userId + " to quantity: " + quantity);

            Cart cart = getOrCreateCart(userId);

            // Ensure items list is not null
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }

            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item != null && item.getId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

            if (quantity <= 0) {
                cart.removeItem(cartItem);
                System.out.println("🗑️ Removed item from cart");
            } else {
                cartItem.setQuantity(quantity);
                System.out.println("📊 Updated item quantity to: " + quantity);
            }

            Cart updatedCart = cartRepository.save(cart);
            return convertToResponse(updatedCart);

        } catch (Exception e) {
            System.err.println("❌ Error updating cart item: " + e.getMessage());
            throw new RuntimeException("Failed to update cart item: " + e.getMessage());
        }
    }

    @Override
    public CartResponse removeItemFromCart(Long userId, Long cartItemId) {
        try {
            System.out.println("🗑️ Removing cart item: " + cartItemId + " for user: " + userId);

            Cart cart = getOrCreateCart(userId);

            // Ensure items list is not null
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }

            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item != null && item.getId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

            cart.removeItem(cartItem);
            Cart updatedCart = cartRepository.save(cart);
            System.out.println("✅ Item removed from cart");
            return convertToResponse(updatedCart);

        } catch (Exception e) {
            System.err.println("❌ Error removing item from cart: " + e.getMessage());
            throw new RuntimeException("Failed to remove item from cart: " + e.getMessage());
        }
    }

    @Override
    public void clearCart(Long userId) {
        try {
            System.out.println("🧹 Clearing cart for user: " + userId);
            Cart cart = getOrCreateCart(userId);
            cart.clearCart();
            cartRepository.save(cart);
            System.out.println("✅ Cart cleared successfully");
        } catch (Exception e) {
            System.err.println("❌ Error clearing cart: " + e.getMessage());
            throw new RuntimeException("Failed to clear cart: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartWithDetails(Long userId) {
        try {
            System.out.println("🔍 Getting cart with details for user: " + userId);
            Cart cart = cartRepository.findByUserIdWithItems(userId)
                    .orElseGet(() -> {
                        System.out.println("🆕 Creating new cart for user: " + userId);
                        return createNewCart(userId);
                    });

            // ENSURE ITEMS LIST IS NEVER NULL
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
                System.out.println("⚠️ Cart items was null, initialized empty list");
            }

            System.out.println("📦 Found " + cart.getItems().size() + " items in cart");
            return convertToResponse(cart);

        } catch (Exception e) {
            System.err.println("❌ Error getting cart with details: " + e.getMessage());
            e.printStackTrace();
            // Return empty cart instead of throwing exception
            return createEmptyCartResponse(userId);
        }
    }

    private Cart getOrCreateCart(Long userId) {
        try {
            return cartRepository.findByUserId(userId)
                    .orElseGet(() -> createNewCart(userId));
        } catch (Exception e) {
            System.err.println("❌ Error in getOrCreateCart: " + e.getMessage());
            return createNewCart(userId);
        }
    }

    private Cart createNewCart(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

            Cart cart = new Cart(user);
            Cart savedCart = cartRepository.save(cart);
            System.out.println("🆕 Created new cart with ID: " + savedCart.getId());
            return savedCart;
        } catch (Exception e) {
            System.err.println("❌ Error creating new cart: " + e.getMessage());
            throw new RuntimeException("Failed to create cart: " + e.getMessage());
        }
    }

    private CartResponse createEmptyCartResponse(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

            System.out.println("📭 Returning empty cart response for user: " + userId);

            return new CartResponse(
                    null, // cart id might be null for empty cart
                    userId,
                    user.getFullName(),
                    Collections.emptyList(), // NEVER NULL
                    BigDecimal.ZERO, // zero total
                    0, // zero items
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            System.err.println("❌ Error creating empty cart response: " + e.getMessage());
            // Return minimal response as fallback
            return new CartResponse(
                    null,
                    userId,
                    "Unknown User",
                    Collections.emptyList(),
                    BigDecimal.ZERO,
                    0,
                    LocalDateTime.now()
            );
        }
    }

    private CartResponse convertToResponse(Cart cart) {
        try {
            // ENSURE ITEMS LIST IS NEVER NULL
            List<CartItemResponse> itemResponses = cart.getItems() == null
                    ? Collections.emptyList()
                    : cart.getItems().stream()
                    .filter(item -> item != null) // Filter out null items
                    .map(this::convertCartItemToResponse)
                    .collect(Collectors.toList());

            int totalItems = cart.getItems() == null
                    ? 0
                    : cart.getItems().stream()
                    .filter(item -> item != null) // Filter out null items
                    .mapToInt(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                    .sum();

            return new CartResponse(
                    cart.getId(),
                    cart.getUser().getId(),
                    cart.getUser().getFullName(),
                    itemResponses, // NEVER NULL
                    cart.getTotalAmount() != null ? cart.getTotalAmount() : BigDecimal.ZERO,
                    totalItems,
                    cart.getUpdatedAt() != null ? cart.getUpdatedAt() : LocalDateTime.now()
            );
        } catch (Exception e) {
            System.err.println("❌ Error converting cart to response: " + e.getMessage());
            // Return empty cart as fallback
            return createEmptyCartResponse(cart.getUser().getId());
        }
    }

    private CartItemResponse convertCartItemToResponse(CartItem cartItem) {
        try {
            if (cartItem == null) {
                System.out.println("⚠️ Warning: Found null cart item");
                return createEmptyCartItemResponse();
            }

            return new CartItemResponse(
                    cartItem.getId(),
                    cartItem.getMenuItem() != null ? cartItem.getMenuItem().getId() : 0L,
                    cartItem.getMenuItem() != null ? cartItem.getMenuItem().getName() : "Unknown Item",
                    cartItem.getMenuItem() != null ? cartItem.getMenuItem().getImageUrl() : "",
                    cartItem.getUnitPrice() != null ? cartItem.getUnitPrice() : BigDecimal.ZERO,
                    cartItem.getQuantity() != null ? cartItem.getQuantity() : 0,
                    cartItem.getSubtotal() != null ? cartItem.getSubtotal() : BigDecimal.ZERO
            );
        } catch (Exception e) {
            System.err.println("❌ Error converting cart item to response: " + e.getMessage());
            return createEmptyCartItemResponse();
        }
    }

    private CartItemResponse createEmptyCartItemResponse() {
        return new CartItemResponse(
                0L,
                0L,
                "Unknown Item",
                "",
                BigDecimal.ZERO,
                0,
                BigDecimal.ZERO
        );
    }
}