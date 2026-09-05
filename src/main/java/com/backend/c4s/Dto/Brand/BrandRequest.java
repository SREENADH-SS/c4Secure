package com.backend.c4s.Dto.Brand;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandRequest {

    @NotBlank(message = "Brand Name is Required")
    private String name;

    private String logoUrl;
}
