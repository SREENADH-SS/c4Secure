package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Cart.CartItemResponse;
import com.backend.c4s.Dto.Cart.CartResponse;
import com.backend.c4s.Entity.Cart;
import com.backend.c4s.Entity.CartItem;
import com.backend.c4s.Entity.Products;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartResponse toCartResponse(Cart cart){
        if(cart== null){
            return null;
        }
        List<CartItemResponse> itemResponses = mapCartItem(cart.getItems());

        BigDecimal totalAmount= itemResponses.stream()
                .map(CartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        int totalItems =itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser() != null ? cart.getUser().getId():null)
                .items(itemResponses)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .build();
    }

    public CartItemResponse toCartItemResponse(CartItem cartItem){
        if (cartItem==null){
            return null;
        }
        Products product= cartItem.getProduct();
        BigDecimal unitPrice= product != null ? product.getPrice() : BigDecimal.ZERO;
        int quantity= cartItem.getQuantity() != null ? cartItem.getQuantity(): 0;
        BigDecimal subTotal= unitPrice.multiply(BigDecimal.valueOf(quantity));

        String imageUrl= null;
        if (product!= null && product.getImages() != null && !product.getImages().isEmpty()){
            imageUrl= product.getImages().get(0).getImageUrl();
        }
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productId(product != null ? product.getId() : null)
                .productName(product !=null ? product.getName() : null)
                .productImageUrl(imageUrl)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .subTotal(subTotal)
                .build();
    }

    private List<CartItemResponse> mapCartItem(List<CartItem> items) {
        if(items== null || items.isEmpty()){
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());
    }
}
