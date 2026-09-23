package com.backend.c4s.Dto.Maintenance;

import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MaintenanceRequest {

    @NotBlank(message = "Issue title is required")
    private String issueTitle;

    @NotBlank(message = "Issue description is required")
    private String issueDescription;

    private String serviceAddress;

    @NotNull(message = "Scheduled date is required")
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledAt;
}
