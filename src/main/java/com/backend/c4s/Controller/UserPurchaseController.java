package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Purchase.CheckoutRequest;
import com.backend.c4s.Dto.Purchase.PackagePurchaseRequest;
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
    ) {
        PurchaseUserResponse response = purchaseService.createPurchase(
                userId,
                request.getShippingAddress(),
                request.getItems(),
                request.getRequiresInstallation()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cart")
    @Operation(summary = "Create Order from User's Cart")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PurchaseUserResponse> createPurchaseFromCart(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CheckoutRequest request
    ) {
        PurchaseUserResponse response = purchaseService.createPurchaseFromCart(
                userId,
                request.getShippingAddress(),
                request.getRequiresInstallation()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/package")
    @Operation(summary = "Purchase a bundled Product Package")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PurchaseUserResponse> createPackagePurchase(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody PackagePurchaseRequest request
    ) {
        PurchaseUserResponse response = purchaseService.createPackagePurchase(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{purchaseId}")
    @Operation(summary = "Get Purchase details of the that user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PurchaseUserResponse> getPurchaseById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable String purchaseId
    ) {
        PurchaseUserResponse response = purchaseService.getPurchaseByIdForUser(purchaseId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get the Details of the All the Orders by that User")
    public ResponseEntity<List<PurchaseUserResponse>> getUserPurchase(
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<PurchaseUserResponse> purchase = purchaseService.getUserPurchases(userId);
        return ResponseEntity.ok(purchase);
    }

    @PatchMapping("/{purchaseId}/cancel")
    @Operation(summary = "Cansel the order of that user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PurchaseUserResponse> cancelOrder(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable String purchaseId
    ) {
        PurchaseUserResponse response = purchaseService.cancelOrder(purchaseId, userId);
        return ResponseEntity.ok(response);
    }
}
