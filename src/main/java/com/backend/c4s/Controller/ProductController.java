package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Product.AdminProductResponse;
import com.backend.c4s.Dto.Product.ProductRequest;
import com.backend.c4s.Dto.Product.ProductResponse;
import com.backend.c4s.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Catalog", description = "Endpoints matching C4SECURE product filtering and gallery displays")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get filtered products (Public)")
    public ResponseEntity<Page<ProductResponse>> getFilteredProducts(
            @RequestParam(value = "brandIds", required = false)List<Long> brandIds,
            @RequestParam(value = "categoryIds", required = false)List<Long> categoryIds,
            @RequestParam(value = "minPrice", required = false)BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false)BigDecimal maxPrice,
            @PageableDefault(size = 9, sort = "createdAt")Pageable pageable
            ){
        return ResponseEntity.ok(productService.getFilteredProducts(brandIds,categoryIds,minPrice,maxPrice,pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID with full gallery images (Public)")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product with Cloudinary images (Admin only)")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestPart("product")ProductRequest request,
            @RequestPart(value = "image", required = false) List<MultipartFile>imagesFiles,
            @RequestParam(value = "primaryImageIndex", defaultValue="0") Integer primaryImageIndex
            ){

        return new ResponseEntity<>(productService.createProductWithImage(request, imagesFiles, primaryImageIndex), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product details & gallery images (Admin only)")
    public ResponseEntity<ProductResponse>updateProduct(
            @PathVariable Long id,
            @Valid @RequestPart("product") ProductRequest request,
            @RequestPart(value = "newImages", required = false)List<MultipartFile>newImages,
            @RequestParam(value = "deleteImageIds", required = false)List<Long> deleteImageIds,
            @RequestParam(value = "newPrimaryImageId", required = false)Long newPrimaryImageId
    ){
        return ResponseEntity.ok(productService.updateProductWithImage(id, request, newImages, deleteImageIds,newPrimaryImageId ));
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get admin product view by ID (Admin only)")
    public ResponseEntity<AdminProductResponse> getAdminProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getAdminProductById(id));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all products list for Admin dashboard (Admin only)")
    public ResponseEntity<Page<AdminProductResponse>> getAllProductForAdmin(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ){
        return ResponseEntity.ok(productService.getAllProductForAdmin(pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product & purge Cloudinary images (Admin only)")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
