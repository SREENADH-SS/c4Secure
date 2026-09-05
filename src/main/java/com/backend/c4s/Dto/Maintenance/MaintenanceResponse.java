package com.backend.c4s.Dto.Maintenance;

import com.backend.c4s.Entity.common.MaintenanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class MaintenanceResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long productId;
    private String issueTitle;
    private String issueDescription;
    private String serviceAdders;
    private LocalDateTime requestedAt;
}
