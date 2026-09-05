package com.backend.c4s.Dto.ServiceReview;

import com.backend.c4s.Entity.common.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdminServiceReviewResponse {
    private Long id;
    private Long userId;
    private String userName;
    private ServiceType serviceType;
    private Long serviceId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
