package com.backend.c4s.Dto.Purchase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackagePurchaseRequest {

    @NotNull(message = "Package ID is required")
    private Long packageId;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    private Boolean requiresInstallation;
}
