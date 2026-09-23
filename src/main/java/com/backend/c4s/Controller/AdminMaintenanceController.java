package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Maintenance.MaintenanceResponse;
import com.backend.c4s.Entity.common.MaintenanceStatus;
import com.backend.c4s.Service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/maintenance")
@RequiredArgsConstructor
@Tag(name = "AminMaintenanceController", description = "Endpoint for the admin control for the Maintenance")
public class AdminMaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "to get all the Maintenance Requests for admin")
    public ResponseEntity<List<MaintenanceResponse>>getAllMaintenanceRequests(
            @RequestParam(required = false)MaintenanceStatus status
            ){
        if (status!=null){
            return ResponseEntity.ok(maintenanceService.getMaintenanceRequestByStatus(status));
        }
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceRequests());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "to update Status by Admin")
    public ResponseEntity<MaintenanceResponse>updateMaintenanceStatus(
            @PathVariable Long id,
            @RequestParam MaintenanceStatus status
    ){
        return ResponseEntity.ok(maintenanceService.updateMaintenanceStatus(id, status));
    }
}
