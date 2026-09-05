package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.WishList.WishListProducts;
import com.backend.c4s.Dto.WishList.WishListResponse;
import com.backend.c4s.Entity.ProductImage;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.WishList;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WishListMapper {

    public WishListResponse toWishListResponse(WishList wishList){
        if (wishList==null){
            return null;
        }
        return WishListResponse.builder()
                .id(wishList.getId())
                .userId(wishList.getUser() != null ? wishList.getUser().getId() : null)
                .products(mapToProductDtos(wishList.getProduct()))
                .build();
    }

     private Set<WishListProducts> mapToProductDtos(Set<Products> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptySet();
        }

        return products.stream()
                .map(product -> WishListProducts.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .price(product.getPrice())
                        .imageUrl(extractImageUrl(product.getImages()))
                        .inStock(product.getStockQuantity() != null && product.getStockQuantity() > 0)
                        .build())
                .collect(Collectors.toSet());
    }

    private String extractImageUrl(List<ProductImage> images) {
        if (images==null|| images.isEmpty()){
            return null;
        }
        ProductImage firstImage = images.get(0);
        return firstImage != null ? firstImage.getImageUrl() : null;
    }
}
