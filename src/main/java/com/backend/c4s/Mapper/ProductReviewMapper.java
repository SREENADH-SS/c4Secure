package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.ProductReview.AdminReviewResponse;
import com.backend.c4s.Dto.ProductReview.PublicReviewResponse;
import com.backend.c4s.Entity.ProductReview;
import org.springframework.stereotype.Component;

@Component
public class ProductReviewMapper {

    public AdminReviewResponse toAdminReviewResponse(ProductReview review){
        if (review==null){
            return null;
        }

        return AdminReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProduct()!=null ? review.getProduct().getId():null)
                .productName(review.getProduct()!=null ? review.getProduct().getName():null)
                .userId(review.getUser()!=null ? review.getUser().getId():null)
                .userName(fullName(review))
                .purchaseId(review.getPurchase().getPurchaseId()!=null ? review.getPurchase().getPurchaseId():null)
                .isVerifiedPurchase(review.getIsVerifiedPurchase())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public PublicReviewResponse toPublicReviewResponse(ProductReview review){
        if (review==null){
            return null;
        }

        return PublicReviewResponse.builder()
                .id(review.getId())
                .userName(fullName(review))
                .rating(review.getRating())
                .comment(review.getComment())
                .isVerifiedPurchase(review.getIsVerifiedPurchase())
                .createdAt(review.getCreatedAt())
                .build();
    }

    private String fullName(ProductReview review) {
        if (review == null || review.getUser() == null) {
            return "Anonymous";
        }

        String firstName = review.getUser().getFirstName() != null ? review.getUser().getFirstName() : "";
        String lastName = review.getUser().getLastName() != null ? review.getUser().getLastName() : "";

        String name = (firstName + " " + lastName).trim();
        return name.isEmpty() ? "Anonymous" : name;
    }
}
