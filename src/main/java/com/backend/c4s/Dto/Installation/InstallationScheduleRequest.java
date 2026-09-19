package com.backend.c4s.Dto.Installation;

import com.backend.c4s.Entity.common.ScheduledStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
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
    private String purchaseId;


    private Long userId;

    @NotNull(message = "Scheduled date is required")
    @FutureOrPresent(message = "Scheduled date must be in the present or future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledDate;


    private String address;

    private String notes;

    private ScheduledStatus status;
}
