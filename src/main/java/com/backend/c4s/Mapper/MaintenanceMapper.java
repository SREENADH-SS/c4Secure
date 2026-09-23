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
                .userId(maintenance.getUser() !=null ? maintenance.getUser().getId(): null)
                .userName(fullName)
                .userEmail(maintenance.getUser() != null ? maintenance.getUser().getEmail():null)
                .productId(maintenance.getProduct()!=null ? maintenance.getProduct().getId(): null)
                .issueTitle(maintenance.getIssueTitle())
                .issueDescription(maintenance.getIssueDescription())
                .serviceAdders(maintenance.getServiceAdders())
                .requestedAt(maintenance.getRequestedAt())
                .build();
    }
}
