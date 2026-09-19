package com.backend.c4s.Repository;

import com.backend.c4s.Entity.InstallationSchedule;
import com.backend.c4s.Entity.common.ScheduledStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstallationScheduleRepository extends JpaRepository<InstallationSchedule, Long> {

    Optional<InstallationSchedule>findByPurchasePurchaseId(String purchaseId);

    List<InstallationSchedule>findByUserId(Long userId);

    List<InstallationSchedule>findByStatus(ScheduledStatus status);

    List<InstallationSchedule>findByScheduledDateBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByPurchasePurchaseId(String purchaseId);

    boolean existsByScheduledDate(LocalDateTime scheduledDate);
}
