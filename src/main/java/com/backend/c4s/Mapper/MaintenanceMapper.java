package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Maintenance.MaintenanceResponse;
import com.backend.c4s.Entity.Maintenance;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public MaintenanceResponse toMaintenanceResponse(Maintenance maintenance){
        if (maintenance ==null){
            return null;
        }
        String fullName= null;
        if(maintenance.getUser()!=null){
            String first= maintenance.getUser().getFirstName() !=null? maintenance.getUser().getFirstName():"";
            String last= maintenance.getUser().getLastName() !=null ? maintenance.getUser().getLastName():"";
            fullName=(first+" "+last).trim();
        }
        return MaintenanceResponse.builder()
                .id(maintenance.getId())
                .userId(maintenance.getUser() != null ? maintenance.getUser().getId() : null)
                .userName(fullName)
                .userEmail(maintenance.getUser() != null ? maintenance.getUser().getEmail() : null)
                .issueTitle(maintenance.getIssueTitle())
                .issueDescription(maintenance.getIssueDescription())
                .serviceAddress(maintenance.getServiceAddress())
                .status(maintenance.getStatus())
                .isCustomer(maintenance.getIsCustomer())
                .requestedAt(maintenance.getRequestedAt())
                .scheduledAt(maintenance.getScheduledAt())
                .resolvedAt(maintenance.getResolvedAt())
                .build();
    }
}
