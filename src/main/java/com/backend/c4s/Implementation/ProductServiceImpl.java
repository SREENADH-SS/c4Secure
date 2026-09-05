package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Product.AdminProductResponse;
import com.backend.c4s.Dto.Product.ProductRequest;
import com.backend.c4s.Dto.Product.ProductResponse;
import com.backend.c4s.Entity.ProductImage;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.common.ProductStatus;
import com.backend.c4s.Exception.BadRequestException;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.ProductMapper;
import com.backend.c4s.Repository.BrandRepository;
import com.backend.c4s.Repository.CategoryRepository;
import com.backend.c4s.Repository.ProductImageRepository;
import com.backend.c4s.Repository.ProductRepository;
import com.backend.c4s.Service.CloudinaryService;
import com.backend.c4s.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;


    @Override
    public ProductResponse createProductWithImage(ProductRequest request, List<MultipartFile> imageFiles, Integer primaryImageIndex) {
        Products product = new Products();
        productMapper.mapRequestToEntity(request, product);

        if (request.getProductStatus() == null) {
            product.setProductStatus(ProductStatus.AVAILABLE);
        }
        Products savedProduct = productRepository.save(product);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            uploadAndSaveImages(savedProduct, imageFiles, primaryImageIndex);
        }
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProductWithImage(
            Long id,
            ProductRequest request,
            List<MultipartFile> newImageFile,
            List<Long> deleteImageIds,
            Long newPrimaryImageId) {
        Products existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductId", "id", id));

        productMapper.mapRequestToEntity(request, existingProduct);

        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long imageId : deleteImageIds) {
                ProductImage image = productImageRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));

                try {
                    cloudinaryService.deleteFile(image.getPublicId());
                    existingProduct.getImages().remove(image);
                    productImageRepository.delete(image);
                } catch (IOException e) {
                    throw new BadRequestException("Failed to remove image from Cloudinary: " + e.getMessage());
                }
            }
        }
        if (newImageFile != null && !newImageFile.isEmpty()) {
            uploadAndSaveImages(existingProduct, newImageFile, null);
        }
        if (newPrimaryImageId != null && existingProduct.getImages() != null) {
            for (ProductImage img : existingProduct.getImages()) {
                img.setPrimary(img.getId().equals(newPrimaryImageId));
            }
        }
        Products updateProduct = productRepository.save(existingProduct);
        return productMapper.toProductResponse(updateProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Products product= productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product", "id", id));
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminProductResponse getAdminProductById(Long id) {
        Products product= productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product", "id", id));
        return productMapper.toAdminProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getFilteredProducts(
            List<Long> brandIds,
            List<Long> categoryIds,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        return productRepository.filterProducts(
                ProductStatus.AVAILABLE,
                (brandIds !=null && brandIds.isEmpty())? null: brandIds,
                (categoryIds !=null && categoryIds.isEmpty()) ? null: categoryIds,
                minPrice,
                maxPrice,
                pageable
        ).map(productMapper::toProductResponse);
    }

    @Override
    public Page<AdminProductResponse> getAllProductForAdmin(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toAdminProductResponse);
    }

    @Override
    public void deleteProduct(Long id) {
        Products product= productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product", "id", id));
        if (product.getImages()!=null){
            for (ProductImage image: product.getImages()){
                try {
                    cloudinaryService.deleteFile(image.getPublicId());
                }
                catch (IOException e){

                }
            }
        }
        productRepository.delete(product);
    }

    private void uploadAndSaveImages(Products savedProduct, List<MultipartFile> imageFiles, Integer primaryImageIndex) {
        if (savedProduct.getImages() == null) {
            savedProduct.setImages(new ArrayList<>());
        }

        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile file = imageFiles.get(i);
            if (file != null && !file.isEmpty()) {
                try {
                    Map<String, Object> result = cloudinaryService.uploadFile(file, "c4s_products");

                    boolean isPrimary = (primaryImageIndex != null && primaryImageIndex == i) ||
                            (savedProduct.getImages().isEmpty() && i == 0);

                    ProductImage image = ProductImage.builder()
                            .product(savedProduct)
                            .publicId((String) result.get("public_id"))
                            .imageUrl((String) result.get("secure_url"))
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .size(file.getSize())
                            .primary(isPrimary)
                            .uploadedAt(LocalDateTime.now())
                            .build();

                    ProductImage savedImage = productImageRepository.save(image);
                    savedProduct.getImages().add(savedImage);
                } catch (IOException e) {
                    throw new BadRequestException("Cloudinary upload failed: " + e.getMessage());
                }
            }
        }
    }
}
