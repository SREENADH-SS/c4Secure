package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Purchase.PurchaseAdminResponse;
import com.backend.c4s.Dto.Purchase.PurchaseUserResponse;
import com.backend.c4s.Dto.PurchaseItem.PurchaseItemResponse;
import com.backend.c4s.Entity.Purchase;
import com.backend.c4s.Entity.PurchaseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PurchaseMapper {

    public  final PurchaseItemMapper purchaseItemMapper;

    public PurchaseUserResponse toPurchaseUserResponse(Purchase purchase){
        if (purchase==null){
            return null;
        }

        return PurchaseUserResponse.builder()
                .purchaseId(purchase.getPurchaseId())
                .totalAmount(purchase.getTotalAmount())
                .status(purchase.getStatus())
                .shippingAddress(purchase.getShippingAddress())
                .purchaseDate(purchase.getPurchaseDate())
                .items(mapPurchaseItem(purchase.getItem()))
                .build();
    }

    public PurchaseAdminResponse toPurchaseAdminResponse(Purchase purchase){
        if (purchase==null){
            return null;
        }
        String fullName=null;
        if (purchase.getUser()!=null){
            String firstName=purchase.getUser().getFirstName()!=null ? purchase.getUser().getFirstName():"";
            String lastName=purchase.getUser().getLastName()!=null ? purchase.getUser().getLastName():"";
            fullName=String.join(" ", firstName, lastName).trim();
        }

        return PurchaseAdminResponse.builder()
                .purchaseId(purchase.getPurchaseId())
                .userId(purchase.getUser().getId()!=null? purchase.getUser().getId() : null)
                .userName(fullName)
                .userEmail(purchase.getUser().getEmail()!=null ? purchase.getUser().getEmail(): null)
                .userPhone(purchase.getUser().getPhone()!=null ? purchase.getUser().getPhone(): null)
                .totalAmount(purchase.getTotalAmount())
                .status(purchase.getStatus())
                .shippingAddress(purchase.getShippingAddress())
                .purchaseDate(purchase.getPurchaseDate())
                .items(mapPurchaseItem(purchase.getItem()))
                .build();
    }

    private List<PurchaseItemResponse> mapPurchaseItem(List<PurchaseItem> item) {
        if (item==null|| item.isEmpty()){
            return Collections.emptyList();
        }
        return item.stream()
                .map(purchaseItemMapper::toPurchaseItemResponse)
                .collect(Collectors.toList());
    }
}
