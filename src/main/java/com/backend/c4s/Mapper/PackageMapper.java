package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Package.PackageItemRequest;
import com.backend.c4s.Dto.Package.PackageItemResponse;
import com.backend.c4s.Dto.Package.PackageRequest;
import com.backend.c4s.Dto.Package.PackageResponse;
import com.backend.c4s.Entity.Package;
import com.backend.c4s.Entity.PackageItem;
import com.backend.c4s.Entity.Products;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PackageMapper {

    public Package toEntity(PackageRequest dto){

        if (dto== null) return null;

        return Package.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .packagePrice(dto.getPackagePrice())
                .installationCharge(dto.getInstallationCharge()!= null? dto.getInstallationCharge(): BigDecimal.ZERO)
                .active(true)
                .build();
    }

    public PackageItem toPackageItemEntity(PackageItemRequest dto, Products product, Package offerPackage){
        if (dto== null) return null;

        return PackageItem.builder()
                .product(product)
                .offerPackage(offerPackage)
                .quantity(dto.getQuantity())
                .build();
    }

    public PackageResponse toResponse(Package entity){
        if (entity==null)return null;

        List<PackageItemResponse> itemResponses= entity.getItems()!= null ? entity.getItems()
                .stream().map(this:: toPackageItemResponse)
                .collect(Collectors.toList())
                : Collections.emptyList();

        BigDecimal originalPrice= itemResponses.stream()
                .map(PackageItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saving= originalPrice.subtract(entity.getPackagePrice()!=null? entity.getPackagePrice(): BigDecimal.ZERO);

        return PackageResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .packagePrice(entity.getPackagePrice())
                .installationCharge(entity.getInstallationCharge())
                .originalPrice(originalPrice)
                .savings(saving)
                .imageUrl(entity.getImageUrl())
                .active(entity.isActive())
                .items(itemResponses)
                .build();
    }

    private PackageItemResponse toPackageItemResponse(PackageItem items) {
        if (items==null)return null;

        Products product= items.getProduct();
        BigDecimal productPrice= (product!= null && product.getPrice() != null)
                ? product.getPrice()
                :BigDecimal.ZERO;

        BigDecimal totalPrice= productPrice.multiply(BigDecimal.valueOf(items.getQuantity()));

        String firstImageUrl = null;
        if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
            firstImageUrl = product.getImages().get(0).getImageUrl(); // Adjust 'getImageUrl()' to your ProductImage getter name
        }

        return PackageItemResponse.builder()
                .productId(product!= null ? product.getId(): null)
                .productName(product!= null ? product.getName(): null)
                .productImageUrl(firstImageUrl)
                .quantity(items.getQuantity())
                .productPrice(productPrice)
                .totalPrice(totalPrice)
                .build();
    }
}
