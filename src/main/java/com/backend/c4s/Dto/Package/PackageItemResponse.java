package com.backend.c4s.Dto.Package;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageItemResponse {

    private Long productId;

    private String productName;

    private String productImageUrl;

    private Integer quantity;

    private BigDecimal productPrice;

    private BigDecimal totalPrice;
}
