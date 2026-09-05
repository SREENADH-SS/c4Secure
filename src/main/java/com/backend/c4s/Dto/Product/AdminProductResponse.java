package com.backend.c4s.Dto.Product;

import com.backend.c4s.Dto.Brand.BrandResponse;
import com.backend.c4s.Dto.Category.CategoryResponse;
import com.backend.c4s.Dto.ProductImage.ProductImageDto;
import com.backend.c4s.Entity.Brand;
import com.backend.c4s.Entity.common.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AdminProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private ProductStatus productStatus;
    private Set<CategoryResponse> categories;
    private List<ProductImageDto> images;
    private BrandResponse brand;

    // Derived review metrics
    private Double averageRating;
    private Integer reviewCount;

    // Audit timestamps from Entity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
