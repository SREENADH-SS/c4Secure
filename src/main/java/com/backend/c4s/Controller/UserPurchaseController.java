package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Purchase.PurchaseCreateRequest;
import com.backend.c4s.Dto.Purchase.PurchaseUserResponse;
import com.backend.c4s.Service.PurchaseService;
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
@RequestMapping("/api/v1/user/purchases")
@RequiredArgsConstructor
@Tag(name = "Management for Purchases for Users", description = "Endpoints for the user accounts for Purchasing the Products")
public class UserPurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @Operation(summary = "Create Order By the user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PurchaseUserResponse> createPurchase(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody PurchaseCreateRequest request
    ){
        PurchaseUserResponse response= purchaseService.createPurchase(
                userId,
                request.getShippingAddress(),
                request.getItems(),
                request.getRequiresInstallation()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{purchaseId}")
    @Operation(summary = "Get Purchase details of the that user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PurchaseUserResponse>getPurchaseById(
            @RequestHeader("X-User-Id")Long userId,
            @PathVariable String purchaseId
    ){
        PurchaseUserResponse response= purchaseService.getPurchaseByIdForUUser(purchaseId,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get the Details of the All the Orders by that User")
    public ResponseEntity<List<PurchaseUserResponse>> getUserPurchase(
            @RequestHeader("X-User-Id")Long userId
    ){
        List<PurchaseUserResponse> purchase= purchaseService.getUserPurchases(userId);
        return ResponseEntity.ok(purchase);
    }

    @PatchMapping("/{purchaseId}/cancel")
    @Operation(summary = "Cansel the order of that user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PurchaseUserResponse>cancelOrder(
            @RequestHeader("X-User-Id")Long userId,
            @PathVariable String purchaseId
    ){
        PurchaseUserResponse response= purchaseService.cancelOrder(purchaseId, userId);
        return ResponseEntity.ok(response);
    }
}
