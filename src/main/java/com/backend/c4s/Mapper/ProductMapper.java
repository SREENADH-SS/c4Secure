package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Brand.BrandResponse;
import com.backend.c4s.Dto.Category.CategoryResponse;
import com.backend.c4s.Dto.Product.AdminProductResponse;
import com.backend.c4s.Dto.Product.ProductRequest;
import com.backend.c4s.Dto.ProductImage.ProductImageDto;
import com.backend.c4s.Dto.Product.ProductResponse;
import com.backend.c4s.Entity.Brand;
import com.backend.c4s.Entity.Category;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.ProductReview;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Repository.BrandRepository;
import com.backend.c4s.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public void mapRequestToEntity(ProductRequest request, Products product) {
        if (request == null || product == null) return;

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setProductStatus(request.getProductStatus());

        // Map Brand relationship
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.getBrandId()));
            product.setBrand(brand);
        } else {
            product.setBrand(null);
        }

        // Map Categories relationship
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryId());
            product.setCategories(new HashSet<>(categories));
        } else {
            product.setCategories(Collections.emptySet());
        }
    }

    public ProductResponse toProductResponse(Products product){
        if(product==null) return null;

        double avgRating= calculateAverageRating(product.getReviews());
        int reviewCount = product.getReviews() !=null ? product.getReviews().size():0;

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .productStatus(product.getProductStatus())
                .brand(mapBrand(product.getBrand()))
                .categories(mapCategories(product))
                .images(mapImages(product))
                .averageRating(avgRating)
                .reviewCount(reviewCount)
                .build();
    }



    public AdminProductResponse toAdminProductResponse(Products product){
        if(product==null) return null;
        double avgRating= calculateAverageRating(product.getReviews());
        int reviewCount = product.getReviews() !=null ? product.getReviews().size():0;

        return AdminProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .productStatus(product.getProductStatus())
                .brand(mapBrand(product.getBrand()))
                .categories(mapCategories(product))
                .images(mapImages(product))
                .averageRating(avgRating)
                .reviewCount(reviewCount)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

   private Set<CategoryResponse> mapCategories(Products product){
            if(product.getCategories()==null) return Collections.emptySet();
        return product.getCategories().stream()
                .map(category -> CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build())
                .collect(Collectors.toSet());

   }


    private List<ProductImageDto> mapImages(Products product) {
        if(product.getImages()== null)return  Collections.emptyList();

        return product.getImages().stream()
                .map(img -> ProductImageDto.builder()
                .id(img.getId())
                .publicId(img.getPublicId())
                .build())
                .collect(Collectors.toList());
    }

    private double calculateAverageRating(List<ProductReview> reviews) {
        if(reviews== null||reviews.isEmpty())return 0.0;
        double sum = reviews.stream().mapToInt(ProductReview::getRating).sum();
        return Math.round((sum/ reviews.size())*10.0)/ 10.0;
    }
    private BrandResponse mapBrand(Brand brand) {
        if (brand == null) return null;

        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                // Map any other fields present in your BrandResponse DTO (e.g., brand.getLogoUrl())
                .build();
    }
}
