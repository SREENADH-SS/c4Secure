package com.backend.c4s.Dto.ServiceReview;

import com.backend.c4s.Entity.common.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PublicServiceReviewResponse {

    private Long id;
    private String userName;
    private Long serviceId;
    private ServiceType serviceType;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
