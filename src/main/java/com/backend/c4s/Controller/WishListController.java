package com.backend.c4s.Controller;

import com.backend.c4s.Dto.WishList.WishListResponse;
import com.backend.c4s.Service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
@Tag(name = "WishList", description = "Endpoints for managing user WishList")
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get All the WishList Product By UserId")
    public ResponseEntity<WishListResponse> getWishList(@PathVariable Long userId){
        return ResponseEntity.ok(wishListService.getWishListByUserId(userId));
    }

    @PostMapping("/{userId}/products/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Add Product To WishList")
    public ResponseEntity<WishListResponse> addProductToWishList(@PathVariable Long userId,@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListService.addProductToWishList(userId,productId));
    }

    @DeleteMapping("/{userId}/products/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Move a Single Product From WishList By UserId")
    public ResponseEntity<WishListResponse>removeProductFromWishList(
            @PathVariable Long userId,
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(wishListService.removeProductFromWishList(userId,productId));
    }

    @DeleteMapping("/{userId}/clear")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Clear All Product From WishList")
    public ResponseEntity<WishListResponse>clearWishList(@PathVariable Long userId){
        return ResponseEntity.ok(wishListService.clearWishList(userId));
    }


    @PostMapping("/{userId}/products/{productId}/add-to-cart")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Add WishList Product To Cart")
    public ResponseEntity<WishListResponse>addProductToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity
            ){
        WishListResponse updatedWishList= wishListService.addProductToCartFromWishList(userId,productId,quantity);
        return ResponseEntity.ok(updatedWishList);
    }

    @PostMapping("/{userId}/move-all-to-cart")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Move All Products From WishList To Cart")
    public ResponseEntity<WishListResponse>moveAllToCart(@PathVariable Long userId){
        WishListResponse updatedWishList= wishListService.moveAllWishListToCart(userId);
        return ResponseEntity.ok(updatedWishList);
    }

}
