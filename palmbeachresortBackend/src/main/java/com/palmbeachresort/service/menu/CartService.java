// [file name]: CartService.java
package com.palmbeachresort.service.menu;

import com.palmbeachresort.dto.menu.CartItemRequest;
import com.palmbeachresort.dto.menu.CartResponse;

public interface CartService {

    CartResponse getCart(Long userId);
    CartResponse addItemToCart(Long userId, CartItemRequest cartItemRequest);
    CartResponse updateCartItem(Long userId, Long cartItemId, Integer quantity);
    CartResponse removeItemFromCart(Long userId, Long cartItemId);
    void clearCart(Long userId);
    CartResponse getCartWithDetails(Long userId);
}