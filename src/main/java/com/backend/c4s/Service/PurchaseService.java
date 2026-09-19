package com.backend.c4s.Service;

import com.backend.c4s.Dto.Purchase.PurchaseAdminResponse;
import com.backend.c4s.Dto.Purchase.PurchaseUserResponse;
import com.backend.c4s.Dto.PurchaseItem.PurchaseItemRequest;
import com.backend.c4s.Entity.common.PurchaseStatus;

import java.util.List;

public interface PurchaseService {

    PurchaseUserResponse createPurchase(Long userId, String shippingAddress, List<PurchaseItemRequest>items, boolean requiresInstallation);

    PurchaseUserResponse getPurchaseByIdForUUser(String purchaseId, Long userId);

    List<PurchaseUserResponse> getUserPurchases(Long userId);

    PurchaseUserResponse cancelOrder(String purchaseId, Long userId);

    List<PurchaseAdminResponse>  getAllPurChasesList();

    PurchaseAdminResponse getPurchaseByIdForAdmin(String purchaseId);

    PurchaseAdminResponse updatePurchaseStatus(String purchaseId, PurchaseStatus status);}
