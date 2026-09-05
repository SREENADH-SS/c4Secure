package com.backend.c4s.Dto.Installation;

import com.backend.c4s.Entity.common.ScheduledStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class InstallationScheduleRequest {
    @NotNull(message = "Purchase ID is required")
    private Long purchaseId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Scheduled date is required")
    @FutureOrPresent(message = "Scheduled date must be in the present or future")
    private LocalDateTime scheduledDate;

    @NotBlank(message = "Address is required")
    private String address;

    private String notes;

    private ScheduledStatus status;
}
