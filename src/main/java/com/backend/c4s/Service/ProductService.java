package com.backend.c4s.Service;

import com.backend.c4s.Dto.Product.AdminProductResponse;
import com.backend.c4s.Dto.Product.ProductRequest;
import com.backend.c4s.Dto.Product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse createProductWithImage(ProductRequest request, List<MultipartFile> imageFiles, Integer primaryImageIndex);

    ProductResponse updateProductWithImage(
            Long id,
            ProductRequest request,
            List<MultipartFile> newImageFile,
            List<Long>deleteImageIds,
            Long newPrimaryImageIds
    );

    ProductResponse getProductById(Long id);
    AdminProductResponse getAdminProductById(Long id);

    Page<ProductResponse>getFilteredProducts(
            List<Long> brandIds,
            List<Long> categoryIds,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<AdminProductResponse>getAllProductForAdmin(Pageable pageable);
    void deleteProduct(Long id);
}
