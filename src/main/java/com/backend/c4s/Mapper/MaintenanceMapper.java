package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Maintenance.MaintenanceResponse;
import com.backend.c4s.Entity.Maintenance;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public MaintenanceResponse toMaintenanceResponse(Maintenance maintenanceRequest){
        if (maintenanceRequest ==null){
            return null;
        }
        String fullName= null;
        if(maintenanceRequest.getUser()!=null){
            String first= maintenanceRequest.getUser().getFirstName() !=null? maintenanceRequest.getUser().getFirstName():"";
            String last= maintenanceRequest.getUser().getLastName() !=null ? maintenanceRequest.getUser().getLastName():"";
            fullName=(first+" "+last).trim();
        }
        return MaintenanceResponse.builder()
                .userId(maintenanceRequest.getUser() !=null ? maintenanceRequest.getUser().getId(): null)
                .userName(fullName)
                .userEmail(maintenanceRequest.getUser() != null ? maintenanceRequest.getUser().getEmail():null)
                .productId(maintenanceRequest.getProduct()!=null ? maintenanceRequest.getProduct().getId(): null)
                .issueTitle(maintenanceRequest.getIssueTitle())
                .issueDescription(maintenanceRequest.getIssueDescription())
                .serviceAdders(maintenanceRequest.getServiceAdders())
                .requestedAt(maintenanceRequest.getRequestedAt())
                .build();
    }
}
