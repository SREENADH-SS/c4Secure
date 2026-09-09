package com.backend.c4s.Service;

import com.backend.c4s.Dto.WishList.WishListRequest;
import com.backend.c4s.Dto.WishList.WishListResponse;

public interface WishListService {

    WishListResponse getWishListByUserId(Long userId);

    WishListResponse addProductToWishList(WishListRequest request);

    WishListResponse removeProductFromWishList(Long userId, Long productId);

    WishListResponse clearWishList(Long userId);

    void addProductToCartFromWishList(Long userId, Long productId, Integer quantity);

    void moveAllWishListToCart(Long userId);
}
