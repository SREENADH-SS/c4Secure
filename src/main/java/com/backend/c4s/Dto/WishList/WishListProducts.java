package com.backend.c4s.Dto.WishList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class WishListProducts {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String imageUrl;
    private Boolean inStock;
}
