package com.backend.c4s.Dto.Package;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal packagePrice;

    @DecimalMin(value = "0.00")
    private BigDecimal installationCharge;

    private MultipartFile image;

    @NotEmpty
    @Valid
    private List<PackageItemRequest> items;
}
