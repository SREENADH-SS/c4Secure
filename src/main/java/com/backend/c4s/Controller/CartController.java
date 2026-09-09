package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Cart.AddToCartRequest;
import com.backend.c4s.Dto.Cart.CartResponse;
import com.backend.c4s.Dto.Cart.UpdateCartItemRequest;
import com.backend.c4s.Service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "APIs for managing security of Cart Items and Cart as per the user id ")
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    @Operation(summary = " Get Cart Details by User ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/user/{userId}/items")
    @Operation(summary = "Add items to the Cart")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request
            ){
        return ResponseEntity.ok(cartService.addToCart(userId,request));
    }

    @PutMapping("/user/{userId}/items/{cartItemId}")
    @Operation(summary = "Update Cart Item Quantity")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request
            ){
        return ResponseEntity.ok(cartService.updateCartItemQuantity(userId,cartItemId,request));
    }

    @DeleteMapping("/user/{userId}/items/{cartItemId}")
    @Operation(summary = "Remove Cart Item From Cart")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId
    ){
        return ResponseEntity.ok(cartService.removeCartItem(userId,cartItemId));
    }

    @DeleteMapping("/user/{userId}/clear")
    @Operation(summary = "Clear All Items From Cart")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
