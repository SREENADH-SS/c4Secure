package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Maintenance.MaintenanceRequest;
import com.backend.c4s.Dto.Maintenance.MaintenanceResponse;
import com.backend.c4s.Entity.Maintenance;
import com.backend.c4s.Entity.Users;
import com.backend.c4s.Entity.common.MaintenanceStatus;
import com.backend.c4s.Entity.common.PurchaseStatus;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.MaintenanceMapper;
import com.backend.c4s.Repository.MaintenanceRepository;
import com.backend.c4s.Repository.PurchaseRepository;
import com.backend.c4s.Repository.UserRepository;
import com.backend.c4s.Service.MaintenanceService;
import com.backend.c4s.Service.SlotValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final SlotValidationService slotValidationService;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceResponse createMaintenanceRequest(Long userId, MaintenanceRequest request) {

        Users user= userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("user", "id", userId));

        boolean isCustomer= purchaseRepository.existsByUserIdAndStatus(userId, PurchaseStatus.COMPLETED);

        LocalDateTime scheduledDate= request.getScheduledAt();
        if (scheduledDate==null || !scheduledDate.isAfter(LocalDateTime.now())){
            throw new IllegalStateException("Maintenance must be scheduled for a future date");
        }

        if (!slotValidationService.isSlotAvailable(scheduledDate)){
            throw new IllegalStateException("The selected time slot is already booked for installation or maintenance.");
        }

        Maintenance maintenance= Maintenance.builder()
                .user(user)
                .issueTitle(request.getIssueTitle())
                .issueDescription(request.getIssueDescription())
                .serviceAddress(request.getServiceAddress())
                .status(MaintenanceStatus.IN_PROGRESS)
                .isCustomer(isCustomer)
                .scheduledAt(scheduledDate)
                .build();

        Maintenance saved= maintenanceRepository.save(maintenance);
        return maintenanceMapper.toMaintenanceResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceResponse getMaintenanceById(Long maintenanceId, Long userId) {
        Maintenance maintenance= maintenanceRepository.findById(maintenanceId)
                .orElseThrow(()->new ResourceNotFoundException("maintenance_request", "id", maintenanceId));
        validUserAccess(maintenance, userId);
        return maintenanceMapper.toMaintenanceResponse(maintenance);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getUserMaintenanceRequest(Long userId) {
        return maintenanceRepository.findByUserId(userId).stream()
                .map(maintenanceMapper::toMaintenanceResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getAllMaintenanceRequests() {
        return maintenanceRepository.findAll().stream().map(maintenanceMapper::toMaintenanceResponse).toList();
    }

    @Override
    public List<MaintenanceResponse> getMaintenanceRequestByStatus( MaintenanceStatus status) {
        return maintenanceRepository.findByStatus(status)
                .stream()
                .map(maintenanceMapper::toMaintenanceResponse)
                .toList();
    }

    @Override
    public MaintenanceResponse rescheduleMaintenance(Long maintenanceId, Long userId, MaintenanceRequest request) {
        Maintenance maintenance= maintenanceRepository.findById(maintenanceId)
                .orElseThrow(()->new ResourceNotFoundException("maintenance_request", "id", maintenanceId));

        validUserAccess(maintenance, userId);

        if (maintenance.getStatus() == MaintenanceStatus.RESOLVED
        || maintenance.getStatus()== MaintenanceStatus.CANCELLED){
            throw new IllegalStateException("Cannot reschedule a resolved or cancelled maintenance request");
        }

        LocalDateTime requestedDate= request.getScheduledAt();
        if (!requestedDate.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Maintenance must be scheduled for a future date");
        }

        if (!slotValidationService.isSlotAvailable(requestedDate)){
            throw new IllegalStateException("The selected time slot is already booked. Please select another date.");
        }

        maintenance.setScheduledAt(requestedDate);
        if (request.getIssueTitle()!= null && !request.getIssueTitle().isBlank()){
            maintenance.setIssueTitle(request.getIssueTitle());
        }
        if (request.getIssueDescription() != null && !request.getIssueDescription().isBlank()) {
            maintenance.setIssueDescription(request.getIssueDescription());
        }
        if (request.getServiceAddress() != null && !request.getServiceAddress().isBlank()) {
            maintenance.setServiceAddress(request.getServiceAddress());
        }

        Maintenance updated= maintenanceRepository.save(maintenance);
        return maintenanceMapper.toMaintenanceResponse(updated);
    }

    @Override
    public MaintenanceResponse updateMaintenanceStatus(Long maintenanceId, MaintenanceStatus status) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("maintenance_request", "id", maintenanceId));

        maintenance.setStatus(status);
        if (status == MaintenanceStatus.RESOLVED || status == MaintenanceStatus.CANCELLED) {
            maintenance.setRequestedAt(LocalDateTime.now()); // Consider renaming field to resolvedAt/updatedAt
        }

        Maintenance updated = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toMaintenanceResponse(updated);

    }

    @Override
    public void cancelMaintenanceRequest(Long maintenanceId, Long userId) {
        Maintenance maintenance= maintenanceRepository.findById(maintenanceId)
                .orElseThrow(()-> new ResourceNotFoundException("maintenance_request", "id", maintenanceId));

        validUserAccess(maintenance, userId);

        if (maintenance.getStatus()== MaintenanceStatus.RESOLVED){
            throw new IllegalStateException("Cannot cancel a completed/resolved maintenance request");
        }

        maintenance.setStatus(MaintenanceStatus.CANCELLED);
        maintenanceRepository.save(maintenance);
    }

    private void validUserAccess(Maintenance maintenance, Long userId) {
        if (!maintenance.getUser().getId().equals(userId)){
            throw new SecurityException("Access denied: Maintenance request does not belong to user");
        }
    }
}
