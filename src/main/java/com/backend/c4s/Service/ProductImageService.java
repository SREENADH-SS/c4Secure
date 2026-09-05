package com.backend.c4s.Service;

import com.backend.c4s.Dto.ProductImage.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    List<ProductImageResponse>getImagesByProductId(Long productId);
    ProductImageResponse getImageById(Long id);
    ProductImageResponse uploadImageToProduct(Long productId, MultipartFile file, boolean isPrimary);
    ProductImageResponse setPrimaryImage(Long productId, Long imageId);
    void deleteImage(Long id);
}
