package com.backend.c4s.Service;

import com.backend.c4s.Dto.Installation.InstallationScheduleRequest;
import com.backend.c4s.Dto.Installation.InstallationScheduleResponse;
import com.backend.c4s.Entity.common.ScheduledStatus;

import java.util.List;

public interface InstallationScheduleService {

    InstallationScheduleResponse scheduleInstallation(Long userId, InstallationScheduleRequest request);

    InstallationScheduleResponse getScheduleById(Long scheduleId, Long userId);

    InstallationScheduleResponse getScheduleByPurchaseId(String purchaseId, Long userId);

    List<InstallationScheduleResponse> getUserSchedules(Long userId);

    List<InstallationScheduleResponse> getAllSchedules();

    List<InstallationScheduleResponse> getScheduleByStatus(ScheduledStatus status);

    InstallationScheduleResponse updateScheduleStatus(Long scheduleId, ScheduledStatus status, String note);

    InstallationScheduleResponse rescheduleInstallation(Long scheduledId, Long userId, InstallationScheduleRequest request);

    void cancelSchedule(Long scheduledId, Long userId);
}
