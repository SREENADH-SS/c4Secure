package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.ProductImage.ProductImageResponse;
import com.backend.c4s.Entity.ProductImage;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Exception.BadRequestException;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.ProductImageMapper;
import com.backend.c4s.Repository.ProductImageRepository;
import com.backend.c4s.Repository.ProductRepository;
import com.backend.c4s.Service.CloudinaryService;
import com.backend.c4s.Service.ProductImageService;
import com.backend.c4s.Utility.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductImageMapper productImageMapper;


    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImagesByProductId(Long productId) {
        if (!productRepository.existsById(productId)){
            throw new ResourceNotFoundException("Product","id", productId);
        }
        return productImageRepository.findByProductId(productId).stream()
                .map(productImageMapper::toProductImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageResponse getImageById(Long id) {
        ProductImage image=productImageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product", "id", id));
        return productImageMapper.toProductImageResponse(image);
    }

    @Override
    public ProductImageResponse uploadImageToProduct(Long productId, MultipartFile file, boolean isPrimary) {
        Products product= productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "id", productId));

        FileUploadUtil.validateImageFile(file);

        try {
            Map<String,Object>uploadResult= cloudinaryService.uploadFile(file,"c4_products");

            if(isPrimary && product.getImages() !=null){
                for (ProductImage img : product.getImages()){
                    img.setPrimary(false);
                }
            }
            ProductImage productImage = ProductImage.builder()
                    .product(product)
                    .publicId((String) uploadResult.get("public_id"))
                    .imageUrl((String) uploadResult.get("secure_url"))
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .primary(isPrimary)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            ProductImage savedImage= productImageRepository.save(productImage);
            return productImageMapper.toProductImageResponse(savedImage);
        } catch (IOException e){
            throw new BadRequestException("Failed to upload image to Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public ProductImageResponse setPrimaryImage(Long productId, Long imageId) {
        List<ProductImage> images= productImageRepository.findByProductId(productId);

        if (images.isEmpty()){
            throw new ResourceNotFoundException("Images for product", "productId", productId);
        }

        ProductImage newPrimary=null;

        for (ProductImage img: images){
            if (img.getId().equals(imageId)){
                img.setPrimary(true);
                newPrimary= img;
            }
            else {
                img.setPrimary(false);
            }
        }
        if (newPrimary== null){
            throw new ResourceNotFoundException("ProductImage", "id", imageId);
        }
        productImageRepository.saveAll(images);
        return productImageMapper.toProductImageResponse(newPrimary);
    }

    @Override
    public void deleteImage(Long id) {
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("ProductImage", "id", id));

        try {
            cloudinaryService.deleteFile(image.getPublicId());
        }
        catch (IOException e){
            throw new BadRequestException("Failed to delete image from Cloudinary: " + e.getMessage());
        }

        productImageRepository.delete(image);

    }
}
