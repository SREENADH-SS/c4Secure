package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.PurchaseItem.PurchaseItemResponse;
import com.backend.c4s.Entity.ProductImage;
import com.backend.c4s.Entity.PurchaseItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PurchaseItemMapper {

    public PurchaseItemResponse toPurchaseItemResponse(PurchaseItem purchaseItem){
        if(purchaseItem==null){
            return null;
        }

        BigDecimal price= purchaseItem.getPriceAtPurchase()!=null? purchaseItem.getPriceAtPurchase(): BigDecimal.ZERO;
        int qty= purchaseItem.getQuantity()!=null ? purchaseItem.getQuantity():0;
        BigDecimal itemTotal= price.multiply(BigDecimal.valueOf(qty));
        String imageUrl=null;

        if (purchaseItem.getProduct()!=null&& purchaseItem.getProduct().getImages()!=null){
            imageUrl=purchaseItem.getProduct()
                    .getImages()
                    .stream()
                    .filter(ProductImage::isPrimary)
                    .map(ProductImage::getImageUrl)
                    .findFirst()
                    .orElse(null);
        }

        return PurchaseItemResponse.builder()
                .id(purchaseItem.getId())
                .productId(purchaseItem.getProduct()!= null? purchaseItem.getProduct().getId() : null)
                .productName(purchaseItem.getProduct()!=null? purchaseItem.getProduct().getName() :null)
                .imageUrl(imageUrl)
                .quantity(purchaseItem.getQuantity())
                .priceAtPurchase(purchaseItem.getPriceAtPurchase())
                .itemTotal(itemTotal)
                .build();
    }
}
