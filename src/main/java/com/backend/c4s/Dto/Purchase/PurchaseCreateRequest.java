package com.backend.c4s.Dto.Purchase;

import com.backend.c4s.Dto.PurchaseItem.PurchaseItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseCreateRequest {

    @NotNull(message = "Shipping address is required")
    private String shippingAddress;

    private Boolean requiresInstallation; // true = installation path, false = skipped path

    @NotEmpty(message = "Purchase items cannot be empty")
    @Valid
    private List<PurchaseItemRequest> items;
}
