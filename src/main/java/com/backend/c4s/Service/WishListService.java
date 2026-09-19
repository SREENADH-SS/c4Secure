package com.backend.c4s.Service;

import com.backend.c4s.Dto.WishList.WishListResponse;

public interface WishListService {

    WishListResponse getWishListByUserId(Long userId);

    WishListResponse addProductToWishList( Long userId, Long productId);

    WishListResponse removeProductFromWishList(Long userId, Long productId);

    WishListResponse clearWishList(Long userId);

    WishListResponse addProductToCartFromWishList(Long userId, Long productId, Integer quantity);

    WishListResponse moveAllWishListToCart(Long userId);
}
