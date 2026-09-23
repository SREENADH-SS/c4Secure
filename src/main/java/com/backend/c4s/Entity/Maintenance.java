package com.backend.c4s.Entity;

import com.backend.c4s.Entity.common.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "maintenance_request")

public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private String issueTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String issueDescription;

    private String serviceAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private MaintenanceStatus status;

    private Boolean isCustomer;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime scheduledAt;

    private LocalDateTime resolvedAt;
}
