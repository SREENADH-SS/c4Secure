package com.backend.c4s.Dto.ProductReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AdminReviewResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Long userId;
    private String userName;
    private String purchaseId;
    private Boolean isVerifiedPurchase;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}

