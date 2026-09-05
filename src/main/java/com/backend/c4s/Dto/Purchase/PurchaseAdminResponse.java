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
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PurchaseAdminResponse {

    private String purchaseId;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private BigDecimal totalAmount;
    private PurchaseStatus status;
    private String shippingAddress;
    private String paymentTransactionId;
    private LocalDateTime purchaseDate;
    private List<PurchaseItemResponse> items;

}
