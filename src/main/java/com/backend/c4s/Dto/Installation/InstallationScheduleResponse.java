package com.backend.c4s.Dto.Installation;

import com.backend.c4s.Entity.common.ScheduledStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class InstallationScheduleResponse {

    private Long id;
    private String purchaseId;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime scheduledDate;
    private String address;
    private String notes;
    private ScheduledStatus status;
}
