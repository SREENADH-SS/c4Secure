package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Installation.InstallationScheduleResponse;
import com.backend.c4s.Entity.common.ScheduledStatus;
import com.backend.c4s.Service.InstallationScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/installations")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Endpoint for the admin to update the installation schedule")
public class AdminInstallationController {

    private final InstallationScheduleService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "To get the all user schedules")
    public ResponseEntity<List<InstallationScheduleResponse>> getAllSchedules(
            @RequestParam(required = false)ScheduledStatus status
            ){
        if (status!= null) {
            return ResponseEntity.ok(service.getScheduleByStatus(status));
        }
        return ResponseEntity.ok(service.getAllSchedules());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update schedule status and notes")
    public ResponseEntity<InstallationScheduleResponse>updateStatus(
            @PathVariable Long id,
            @RequestParam ScheduledStatus status,
            @RequestParam(required = false) String notes
    ){
        return ResponseEntity.ok(service.updateScheduleStatus(id, status, notes));
    }

}
