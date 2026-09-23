package com.backend.c4s.Service;

import com.backend.c4s.Repository.InstallationScheduleRepository;
import com.backend.c4s.Repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SlotValidationService {

    private final InstallationScheduleRepository installationScheduleRepository;

    private final MaintenanceRepository maintenanceRepository;

    public boolean isSlotAvailable(LocalDateTime requestedDate) {
        boolean installationExists = installationScheduleRepository.existsByScheduledDate(requestedDate);
        boolean maintenanceExists = maintenanceRepository.existsByScheduledAt(requestedDate);

        return !installationExists && !maintenanceExists;
    }
}
