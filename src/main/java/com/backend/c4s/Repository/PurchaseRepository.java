package com.backend.c4s.Repository;

import com.backend.c4s.Entity.Purchase;
import com.backend.c4s.Entity.common.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

    List<Purchase>findByUserId(Long userId);

    List<Purchase>findByStatus(PurchaseStatus status);

    boolean existsByPurchaseIdAndUserId(String purchaseId, Long userId);
}
