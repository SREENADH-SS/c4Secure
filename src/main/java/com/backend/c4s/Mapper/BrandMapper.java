package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Brand.BrandRequest;
import com.backend.c4s.Dto.Brand.BrandResponse;
import com.backend.c4s.Entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toEntity(BrandRequest request){
        if(request== null) return null;

        return Brand.builder()
                .name(request.getName())
                .build();
    }

    public BrandResponse toBrandResponse(Brand brand){
        if(brand==null)return null;

        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .logoUrl(brand.getLogoUrl())
                .build();
    }

    public void updateBrandFromRequest(BrandRequest request, Brand brand){
        if (request==null || brand==null) return;

        brand.setName(request.getName());
    }
}
