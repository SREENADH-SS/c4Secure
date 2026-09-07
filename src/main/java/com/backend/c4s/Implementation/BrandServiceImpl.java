package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Brand.BrandRequest;
import com.backend.c4s.Dto.Brand.BrandResponse;
import com.backend.c4s.Entity.Brand;
import com.backend.c4s.Exception.BadRequestException;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.BrandMapper;
import com.backend.c4s.Repository.BrandRepository;
import com.backend.c4s.Service.BrandService;
import com.backend.c4s.Service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final CloudinaryService cloudinaryService;
    private static final String CLOUDINARY_FOLDER = "brands";

    @Override
    public BrandResponse createBrand(BrandRequest request) {
      if (brandRepository.existsByName(request.getName())) {
          throw new BadRequestException("Brand with Name '" + request.getName() + "'already exists.");
      }
      Brand brand= brandMapper.toEntity(request);
        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            String logoUrl = uploadLogoToCloudinary(request.getLogo());
            brand.setLogoUrl(logoUrl);
        }
        Brand savedBrand = brandRepository.save(brand);
      return brandMapper.toBrandResponse(savedBrand);
    }

    @Override
    public BrandResponse updateBrand(Long id, BrandRequest request) {

        Brand brand= brandRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Brand", "id", id));

        if (!brand.getName().equalsIgnoreCase(request.getName()) && brandRepository.existsByName(request.getName())){
            throw new BadRequestException("Brand name '" + request.getName() + "' is already taken.");
        }

        brandMapper.updateBrandFromRequest(request, brand);
        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            // Remove old image from Cloudinary if it exists
            if (brand.getLogoUrl() != null && !brand.getLogoUrl().isBlank()) {
                deleteLogoFromCloudinary(brand.getLogoUrl());
            }

            // Upload new image
            String newLogoUrl = uploadLogoToCloudinary(request.getLogo());
            brand.setLogoUrl(newLogoUrl);
        }
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

        if (brand.getLogoUrl() != null && !brand.getLogoUrl().isBlank()) {
            deleteLogoFromCloudinary(brand.getLogoUrl());
        }

        brandRepository.delete(brand);

    }
    private String uploadLogoToCloudinary(MultipartFile logo) {
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(logo, CLOUDINARY_FOLDER);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload image to Cloudinary: " + e.getMessage());
        }
    }

    private void deleteLogoFromCloudinary(String logoUrl) {
        try {
            String publicId = extractPublicIdFromUrl(logoUrl);
            if (publicId != null) {
                cloudinaryService.deleteFile(publicId);
            }
        } catch (IOException e) {
            // Log warning here if logger is configured
            System.err.println("Could not delete image from Cloudinary: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String logoUrl) {

        try {
            int uploadIndex = logoUrl.indexOf("/upload/");
            if (uploadIndex == -1) return null;

            String pathAfterUpload = logoUrl.substring(uploadIndex + 8);
            // Remove version tag if present (e.g. "v123456789/")
            if (pathAfterUpload.startsWith("v")) {
                pathAfterUpload = pathAfterUpload.substring(pathAfterUpload.indexOf("/") + 1);
            }

            // Remove file extension
            int lastDotIndex = pathAfterUpload.lastIndexOf(".");
            if (lastDotIndex != -1) {
                pathAfterUpload = pathAfterUpload.substring(0, lastDotIndex);
            }

            return pathAfterUpload;
        } catch (Exception e) {
            return null;
        }
    }

}
