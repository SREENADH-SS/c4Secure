package com.backend.c4s.Controller;

import com.backend.c4s.Dto.ProductImage.ProductImageResponse;
import com.backend.c4s.Service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
@Tag(name = "Product Image Management", description = "APIs for individual product image management and Cloudinary sync")
public class ProductImageController {

    private ProductImageService productImageService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all images for a product (Public)")
    public ResponseEntity<List<ProductImageResponse>> getImageByProductId(@PathVariable Long productId){
        return ResponseEntity.ok(productImageService.getImagesByProductId(productId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get single image details by ID (Public)")
    public ResponseEntity<ProductImageResponse> getImageById(@PathVariable Long id){
        return ResponseEntity.ok(productImageService.getImageById(id));
    }

    @PostMapping("/product/{productId},consumes = MediaType.MULTIPART_FORM_DATA_VALUE")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload image for a product (Admin only)")
    public ResponseEntity<ProductImageResponse> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file")MultipartFile file,
            @RequestParam(value = "isPrimary", defaultValue = "false")boolean isPrime){

        return new ResponseEntity<>(
                productImageService.uploadImageToProduct(productId, file, isPrime),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/product/{productId}/primary/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set an image as primary thumbnail (Admin only)")
    public ResponseEntity<ProductImageResponse> setPrimaryImage(
            @PathVariable Long productId,
            @PathVariable Long imageId){

        return ResponseEntity.ok(productImageService.setPrimaryImage(productId, imageId));
    }

    @DeleteMapping("/[id]")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete image by ID and purge Cloudinary asset (Admin only)")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id){
        productImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
