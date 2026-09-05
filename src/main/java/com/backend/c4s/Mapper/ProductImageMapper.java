package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.ProductImage.ProductImageResponse;
import com.backend.c4s.Entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImageResponse toProductImageResponse(ProductImage productImage){
        if(productImage== null){
            return null;
        }
        return ProductImageResponse.builder()
                .id(productImage.getId())
                .productId(productImage.getProduct().getId()!=null ? productImage.getProduct().getId(): null)
                .fileName(productImage.getFileName())
                .imageUrl(productImage.getImageUrl())
                .fileType(productImage.getFileType())
                .size(productImage.getSize())
                .uploadedAt(productImage.getUploadedAt())
                .build();
    }
}
