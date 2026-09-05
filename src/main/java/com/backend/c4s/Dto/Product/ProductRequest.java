package com.backend.c4s.Dto.Product;

import com.backend.c4s.Entity.Brand;
import com.backend.c4s.Entity.common.ProductStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product Name is Required")
    @Size(max=150, message = "Name Must Not Exceed 150 Characters")
    private String name;


    @NotNull(message = "Description Must Be Required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;


    @NotNull(message = "StockQuantity must Be Required")
    @Min(value = 0, message = "Stock Quantity Can Not be a Negative Value")
    private Integer stockQuantity;


    @NotNull(message = "Product Status Must Be Required")
    private ProductStatus productStatus;

    @NotNull(message = "Product Brand Must Be Required")
    private Long brandId;

    @NotEmpty(message = "At Least One Stock Quantity is Required")
    private Set<Long>categoryId;

    private List<String> imageUrls;


}
