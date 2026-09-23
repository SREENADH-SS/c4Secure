package com.backend.c4s.Dto.Purchase;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    private Boolean requiresInstallation;
}
