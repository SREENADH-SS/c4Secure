package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Maintenance.MaintenanceRequest;
import com.backend.c4s.Dto.Maintenance.MaintenanceResponse;
import com.backend.c4s.Service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/maintenance")
@RequiredArgsConstructor
@Tag(name = "user maintenance", description = "An endpoint for user for Maintenance booking slot")
public class UserMaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "create and book for maintenance")
    public ResponseEntity<MaintenanceResponse> createMaintenance(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody MaintenanceRequest request
            ){
        MaintenanceResponse response= maintenanceService.createMaintenanceRequest(userId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "to get the user Maintenance requests")
    public ResponseEntity<List<MaintenanceResponse>>getUserMaintenanceRequests(
            @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(maintenanceService.getUserMaintenanceRequest(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "to get the Maintenance by id")
    public ResponseEntity<MaintenanceResponse>getMaintenanceById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(maintenanceService.getMaintenanceById(id, userId));
    }

    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "to reschedule the installation slot")
    public ResponseEntity<MaintenanceResponse> rescheduleMaintenance(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody MaintenanceRequest request
    ){
        return ResponseEntity.ok(maintenanceService.rescheduleMaintenance(id,userId,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> cancelMaintenanceRequest(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ){
        maintenanceService.cancelMaintenanceRequest(id, userId);
        return ResponseEntity.noContent().build();
    }
}
