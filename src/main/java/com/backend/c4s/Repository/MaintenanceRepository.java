package com.backend.c4s.Repository;

import com.backend.c4s.Entity.Maintenance;
import com.backend.c4s.Entity.common.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByUserId(Long userId);

    List<Maintenance> findByStatus(MaintenanceStatus status);

    boolean existsByScheduledAt(LocalDateTime scheduledAt);
}
