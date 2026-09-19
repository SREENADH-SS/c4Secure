package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Installation.InstallationScheduleRequest;
import com.backend.c4s.Dto.Installation.InstallationScheduleResponse;
import com.backend.c4s.Service.InstallationScheduleService;
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
@RequestMapping("/api/v1/user/installation")
@RequiredArgsConstructor
@Tag(name = "Management to book installation schedule", description = "Endpoints for the user accounts for Book installation schedule")
public class UserInstallationController {

    private final InstallationScheduleService installationScheduleService;

    @PostMapping
    @Operation(summary = "Book schedule by User")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<InstallationScheduleResponse> scheduleInstallation(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody InstallationScheduleRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(installationScheduleService.scheduleInstallation(userId,request));
    }

    @GetMapping
    @Operation(summary = "Get User Schedules")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<InstallationScheduleResponse>>getUserSchedule(
            @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(installationScheduleService.getUserSchedules(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get Schedule By ID")
    public ResponseEntity<InstallationScheduleResponse> getScheduleById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(installationScheduleService.getScheduleById(id, userId));
    }

    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "reschedule the installation date by the user")
    public ResponseEntity<InstallationScheduleResponse>reschedule(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody InstallationScheduleRequest request
    ){
        return ResponseEntity.ok(installationScheduleService.rescheduleInstallation(id, userId, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Cansel the schedule of Installation")
    public ResponseEntity<Valid> canselSchedule(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ){
        installationScheduleService.cancelSchedule(id, userId);
        return ResponseEntity.noContent().build();
    }
}
