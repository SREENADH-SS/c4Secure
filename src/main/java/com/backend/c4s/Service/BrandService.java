package com.backend.c4s.Service;

import com.backend.c4s.Dto.Brand.BrandRequest;
import com.backend.c4s.Dto.Brand.BrandResponse;

import java.util.List;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);
    BrandResponse updateBrand( Long id ,BrandRequest request);
    BrandResponse getBrandById(Long id);
    List<BrandResponse> getAlLBrands();
    void deleteBrand(Long id);
}
