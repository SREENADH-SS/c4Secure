package com.backend.c4s.Controller;


import com.backend.c4s.Dto.Purchase.PurchaseAdminResponse;
import com.backend.c4s.Entity.common.PurchaseStatus;
import com.backend.c4s.Service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchases Admin panel", description = "Endpoints for managing user Purchase for Admin")
public class AdminPurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    @Operation(summary = "Get all List of Purchases for admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseAdminResponse>> getAllPurchases(){
        List<PurchaseAdminResponse> purchases= purchaseService.getAllPurChasesList();

        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/{purchaseId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get PurchaseDetails of a single user By Id")
    public ResponseEntity<PurchaseAdminResponse>getPurchaseById(@PathVariable String purchaseId){
        PurchaseAdminResponse response= purchaseService.getPurchaseByIdForAdmin(purchaseId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{purchaseId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update the Order Status of the user Purchases")
    public ResponseEntity<PurchaseAdminResponse> updatePurchaseStatus(
            @PathVariable String purchaseId,
            @RequestParam PurchaseStatus status
            ){

        PurchaseAdminResponse updatedPurchase= purchaseService.updatePurchaseStatus(purchaseId,status);
        return ResponseEntity.ok(updatedPurchase);
    }
}
