package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Installation.InstallationScheduleResponse;
import com.backend.c4s.Entity.InstallationSchedule;
import org.springframework.stereotype.Component;

@Component
public class InstallationScheduleMapper {

    public InstallationScheduleResponse toInstallationScheduleResponse(InstallationSchedule installationSchedule){
        if(installationSchedule == null){
            return null;
        }

        String fullName = null;
        if (installationSchedule.getUser() != null) {
            String first = installationSchedule.getUser().getFirstName() != null ? installationSchedule.getUser().getFirstName() : "";
            String last = installationSchedule.getUser().getLastName() != null ? installationSchedule.getUser().getLastName() : "";
            fullName = (first + " " + last).trim();
        }
        return InstallationScheduleResponse.builder()
                .id(installationSchedule.getId())
                .purchaseId(installationSchedule.getPurchase() != null ? installationSchedule.getPurchase().getPurchaseId() : null)
                .userId(installationSchedule.getUser() != null ? installationSchedule.getUser().getId() : null)
                .userName(fullName)
                .userEmail(installationSchedule.getUser() != null ? installationSchedule.getUser().getEmail() : null)
                .scheduledDate(installationSchedule.getScheduledDate())
                .address(installationSchedule.getAddress())
                .notes(installationSchedule.getNotes())
                .status(installationSchedule.getStatus())
                .build();
    }
}
