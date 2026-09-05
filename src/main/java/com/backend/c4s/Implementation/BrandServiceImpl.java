package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Brand.BrandRequest;
import com.backend.c4s.Dto.Brand.BrandResponse;
import com.backend.c4s.Entity.Brand;
import com.backend.c4s.Exception.BadRequestException;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.BrandMapper;
import com.backend.c4s.Repository.BrandRepository;
import com.backend.c4s.Service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandResponse createBrand(BrandRequest request) {
      if (brandRepository.existByName(request.getName())) {
          throw new BadRequestException("Brand with Name '" + request.getName() + "'already exists.");
      }
      Brand brand= brandMapper.toEntity(request);
      Brand savedBrand= brandRepository.save(brand);

      return brandMapper.toBrandResponse(savedBrand);
    }

    @Override
    public BrandResponse updateBrand(Long id, BrandRequest request) {

        Brand brand= brandRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Brand", "id", id));

        if (!brand.getName().equalsIgnoreCase(request.getName()) && brandRepository.existByName(request.getName())){
            throw new BadRequestException("Brand name '" + request.getName() + "' is already taken.");
        }

        brandMapper.updateBrandFromRequest(request, brand);
        Brand updateBrand= brandRepository.save(brand);

        return brandMapper.toBrandResponse(updateBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getBrandById(Long id) {

        Brand brand= brandRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Brand", "id", id));

        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public List<BrandResponse> getAlLBrands() {

        return brandRepository.findAll().stream()
                .map(brandMapper::toBrandResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBrand(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Brand", "id", id));

        brandRepository.delete(brand);

    }
}
