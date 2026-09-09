package com.backend.c4s.Service;

import com.backend.c4s.Dto.Cart.AddToCartRequest;
import com.backend.c4s.Dto.Cart.CartResponse;
import com.backend.c4s.Dto.Cart.UpdateCartItemRequest;

public interface CartService {

    CartResponse getCartByUserId(Long userId);

    CartResponse addToCart(Long userId, AddToCartRequest request);

    CartResponse updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemRequest request);

    CartResponse removeCartItem (Long userId, Long cartItemId);

    void clearCart(Long userId);
}
