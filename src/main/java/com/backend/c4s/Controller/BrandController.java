package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Brand.BrandRequest;
import com.backend.c4s.Dto.Brand.BrandResponse;
import com.backend.c4s.Service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Tag(name = "Brand Management", description = "APIs for managing security hardware brands")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "Get All Brands (public)")
    public ResponseEntity<List<BrandResponse>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAlLBrands());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID (Public)")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable Long id ){
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new brand (Admin only)")
    public ResponseEntity<BrandResponse> createBrand(@Valid @ModelAttribute BrandRequest request){
        return new ResponseEntity<>(brandService.createBrand(request), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing brand (Admin only)")
    public ResponseEntity<BrandResponse>updateBrand(@PathVariable Long id, @Valid @ModelAttribute BrandRequest request ){
        return ResponseEntity.ok(brandService.updateBrand(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete brand by ID (Admin only)")
    public ResponseEntity<Void>deleteBrand(@PathVariable Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

}
