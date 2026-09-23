package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Installation.InstallationScheduleRequest;
import com.backend.c4s.Dto.Installation.InstallationScheduleResponse;
import com.backend.c4s.Entity.InstallationSchedule;
import com.backend.c4s.Entity.Purchase;
import com.backend.c4s.Entity.Users;
import com.backend.c4s.Entity.common.PurchaseStatus;
import com.backend.c4s.Entity.common.ScheduledStatus;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.InstallationScheduleMapper;
import com.backend.c4s.Repository.InstallationScheduleRepository;
import com.backend.c4s.Repository.PurchaseRepository;
import com.backend.c4s.Repository.UserRepository;
import com.backend.c4s.Service.InstallationScheduleService;
import com.backend.c4s.Service.SlotValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InstallationScheduleServiceImpl implements InstallationScheduleService {

    private final InstallationScheduleRepository scheduleRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final InstallationScheduleMapper mapper;
    private final SlotValidationService slotValidationService;

    @Override
    public InstallationScheduleResponse scheduleInstallation(Long userId, InstallationScheduleRequest request) {

        Purchase purchase= purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(()-> new ResourceNotFoundException("purchase", "id", request.getPurchaseId()));


        if (!purchase.getUser().getId().equals(userId)){
            throw new SecurityException("Access denied: Order does not belong to user");
        }

        if (purchase.getStatus()!= PurchaseStatus.PENDING_INSTALLATION){
            throw new IllegalStateException("Purchase is not eligible for installation scheduling");
        }

        if (scheduleRepository.existsByPurchasePurchaseId(request.getPurchaseId())){
            throw new IllegalStateException("An installation schedule already exists for this purchase");
        }

        LocalDateTime requestedDate = request.getScheduledDate();

        if (requestedDate == null || !requestedDate.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Installation must be scheduled for a future date");
        }

        // Cross-domain slot validation check (checks both Installation and Maintenance tables)
        if (!slotValidationService.isSlotAvailable(requestedDate)) {
            throw new IllegalStateException("This time slot is already booked for installation or maintenance.");
        }

        Users user= userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("user", "id", userId));

        String shippingAddress =
                request.getAddress() != null && !request.getAddress().isBlank()
                        ? request.getAddress()
                        : purchase.getShippingAddress();

        InstallationSchedule schedule= InstallationSchedule.builder()
                .purchase(purchase)
                .user(user)
                .address(shippingAddress)
                .scheduledDate(requestedDate)
                .notes(request.getNotes())
                .status(ScheduledStatus.SCHEDULED)
                .build();

        purchase.setStatus(PurchaseStatus.SCHEDULED);
        purchaseRepository.save(purchase);

        InstallationSchedule savedSchedule= scheduleRepository.save(schedule);
        return mapper.toInstallationScheduleResponse(savedSchedule);

    }

    @Override
    @Transactional(readOnly = true)
    public InstallationScheduleResponse getScheduleById(Long scheduleId, Long userId) {
        InstallationSchedule schedule= scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new ResourceNotFoundException("installation_schedule", "id", scheduleId));
        validateUserAccess(schedule, userId);
        return mapper.toInstallationScheduleResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public InstallationScheduleResponse getScheduleByPurchaseId(String purchaseId, Long userId) {
        InstallationSchedule schedule= scheduleRepository.findByPurchasePurchaseId(purchaseId)
                .orElseThrow(()-> new ResourceNotFoundException("installation_schedule", "purchaseId", purchaseId));
        validateUserAccess(schedule,userId);
        return mapper.toInstallationScheduleResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstallationScheduleResponse> getUserSchedules(Long userId) {
        return scheduleRepository.findByUserId(userId).stream()
                .map(mapper::toInstallationScheduleResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstallationScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(mapper::toInstallationScheduleResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstallationScheduleResponse> getScheduleByStatus(ScheduledStatus status) {
         return scheduleRepository.findByStatus(status).stream()
                 .map(mapper::toInstallationScheduleResponse)
                 .toList();
    }

    @Override
    public InstallationScheduleResponse updateScheduleStatus(Long scheduleId, ScheduledStatus status, String note) {
        InstallationSchedule schedule= scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new ResourceNotFoundException("installation_schedule", "id", scheduleId));

        schedule.setStatus(status);

        if (note != null && !note.isBlank()){
            schedule.setNotes(note);
        }

        Purchase purchase = schedule.getPurchase();

        if (status== ScheduledStatus.COMPLETED){
            schedule.setCompletedDate(LocalDateTime.now());
            purchase.setStatus(PurchaseStatus.COMPLETED);
            purchaseRepository.save(purchase);
        }
        else if (status == ScheduledStatus.CANCELLED) {
            purchase.setStatus(PurchaseStatus.CANCELLED);
            purchaseRepository.save(purchase);
        }
        InstallationSchedule updated= scheduleRepository.save(schedule);
        return mapper.toInstallationScheduleResponse(updated);
    }

    @Override
    public InstallationScheduleResponse rescheduleInstallation(Long scheduledId, Long userId, InstallationScheduleRequest request) {

        InstallationSchedule schedule= scheduleRepository.findById(scheduledId)
                .orElseThrow(()-> new ResourceNotFoundException("installation_schedule", "id", scheduledId));

        validateUserAccess(schedule, userId);

        Purchase purchase = schedule.getPurchase();

        if (schedule.getStatus() == ScheduledStatus.COMPLETED
                || schedule.getStatus() == ScheduledStatus.CANCELLED
                || purchase.getStatus() == PurchaseStatus.COMPLETED
                || purchase.getStatus() == PurchaseStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Installation or purchase is already completed or cancelled Cannot reschedule a completed or cancelled installation"
            );
        }

        LocalDateTime requestedDate = request.getScheduledDate();

        if (requestedDate == null || !requestedDate.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Installation must be scheduled for a future date");
        }

        // Cross-domain slot validation check
        if (!slotValidationService.isSlotAvailable(requestedDate)) {
            throw new IllegalStateException("This time slot is already booked. Please select another date.");
        }

        schedule.setScheduledDate(requestedDate);

        if(request.getAddress()!= null && !request.getAddress().isBlank()){
            schedule.setAddress(request.getAddress());
        }
        else {
            schedule.setAddress(purchase.getShippingAddress());
        }

        if (request.getNotes()!= null){
            schedule.setNotes(request.getNotes());
        }

        schedule.setStatus(ScheduledStatus.RESCHEDULED);

        InstallationSchedule updated= scheduleRepository.save(schedule);
        return mapper.toInstallationScheduleResponse(updated);
    }

    @Override
    public void cancelSchedule(Long scheduledId, Long userId) {

        InstallationSchedule schedule= scheduleRepository.findById(scheduledId)
                .orElseThrow(()->new ResourceNotFoundException("installation_schedule", "id", scheduledId));

        validateUserAccess(schedule, userId);

        if (schedule.getStatus()== ScheduledStatus.COMPLETED){
            throw new IllegalStateException("Cannot cancel a completed installation");
        }

        schedule.setStatus(ScheduledStatus.CANCELLED);
        Purchase purchase = schedule.getPurchase();
        purchase.setStatus(PurchaseStatus.CANCELLED);
        purchaseRepository.save(purchase);
        scheduleRepository.save(schedule);
    }

    private void validateUserAccess(InstallationSchedule schedule, Long userId) {

        if (!schedule.getUser().getId().equals(userId)){
            throw new SecurityException("Access denied: Installation schedule does not belong to user");
        }
    }
}
