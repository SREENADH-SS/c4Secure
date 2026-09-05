package com.backend.c4s.Dto.Purchase;

import com.backend.c4s.Dto.PurchaseItem.PurchaseItemResponse;
import com.backend.c4s.Entity.common.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PurchaseUserResponse {

    private String purchaseId;
    private BigDecimal totalAmount;
    private PurchaseStatus status;
    private String shippingAddress;
    private LocalDateTime purchaseDate;
    private List<PurchaseItemResponse> items;
}
