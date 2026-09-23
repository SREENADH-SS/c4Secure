package com.backend.c4s.Service;

import com.backend.c4s.Dto.Maintenance.MaintenanceRequest;
import com.backend.c4s.Dto.Maintenance.MaintenanceResponse;
import com.backend.c4s.Entity.common.MaintenanceStatus;

import java.util.List;

public interface MaintenanceService {

    MaintenanceResponse createMaintenanceRequest(Long userId, MaintenanceRequest request);

    MaintenanceResponse getMaintenanceById(Long maintenanceId, Long userId);

    List<MaintenanceResponse> getUserMaintenanceRequest(Long userId);

    List<MaintenanceResponse>getAllMaintenanceRequests();

    List<MaintenanceResponse> getMaintenanceRequestByStatus( MaintenanceStatus status);

    MaintenanceResponse rescheduleMaintenance(Long maintenanceId, Long userId, MaintenanceRequest request);

    MaintenanceResponse updateMaintenanceStatus(Long maintenanceId, MaintenanceStatus status);

    void cancelMaintenanceRequest (Long maintenanceId, Long userId);
}
