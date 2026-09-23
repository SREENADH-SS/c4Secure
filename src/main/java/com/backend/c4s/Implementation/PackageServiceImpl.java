package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Package.PackageItemRequest;
import com.backend.c4s.Dto.Package.PackageRequest;
import com.backend.c4s.Dto.Package.PackageResponse;
import com.backend.c4s.Entity.Package;
import com.backend.c4s.Entity.PackageItem;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Mapper.PackageMapper;
import com.backend.c4s.Repository.PackageRepository;
import com.backend.c4s.Repository.ProductRepository;
import com.backend.c4s.Service.CloudinaryService;
import com.backend.c4s.Service.PackageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final ProductRepository productRepository;
    private final PackageMapper packageMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public PackageResponse createPackage(PackageRequest request) {

        Package pkg = packageMapper.toEntity(request);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinaryService.uploadFile(request.getImage(), request.getName());
                pkg.setImageUrl((String) uploadResult.get("url"));
                pkg.setPublicId((String) uploadResult.get("public_id"));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload package image", e);
            }
        }

        List<PackageItem> items = buildPackageItems(request.getItems(), pkg);
        pkg.setItems(items);

        Package savedPackage = packageRepository.save(pkg);
        return packageMapper.toResponse(savedPackage);
    }

    @Override
    @Transactional(readOnly = true)
    public PackageResponse getPackageById(Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Package not found with ID: " + id));
        return packageMapper.toResponse(pkg);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageResponse> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(packageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageResponse> getActivePackages() {
        return packageRepository.findByActiveTrue().stream()
                .map(packageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PackageResponse updatePackage(Long id, PackageRequest request) {
        Package existingPackage = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Package not found with ID: " + id));

        existingPackage.setName(request.getName());
        existingPackage.setPackagePrice(request.getPackagePrice());
        existingPackage.setDescription(request.getDescription());
        if (request.getInstallationCharge() != null) {
            existingPackage.setInstallationCharge(request.getInstallationCharge());

        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                if (existingPackage.getPublicId() != null) {
                    cloudinaryService.deleteFile(existingPackage.getPublicId());
                }
                Map<?, ?> uploadResult = cloudinaryService.uploadFile(request.getImage(), request.getName());
                existingPackage.setImageUrl((String) uploadResult.get("url"));
                existingPackage.setPublicId((String) uploadResult.get("public_id"));
            } catch (IOException e) {
                throw new RuntimeException("Failed to update package image", e);
            }
        }

        existingPackage.getItems().clear();
        List<PackageItem> updatedItems = buildPackageItems(request.getItems(), existingPackage);
        existingPackage.getItems().addAll(updatedItems);

        Package updatedPackage = packageRepository.save(existingPackage);
        return packageMapper.toResponse(updatedPackage);
    }

    @Override
    @Transactional
    public PackageResponse togglePackageStatus(Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Package not found with ID: " + id));
        pkg.setActive(!pkg.isActive());
        return packageMapper.toResponse(packageRepository.save(pkg));
    }

    @Override
    @Transactional
    public void deletePackage(Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Package not found with ID: " + id));

        if (pkg.getPublicId() != null) {
            try {
                cloudinaryService.deleteFile(pkg.getPublicId());
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete package image from Cloudinary", e);
            }
        }
        packageRepository.delete(pkg);
    }

    private List<PackageItem> buildPackageItems(@NotEmpty @Valid List<PackageItemRequest> items, Package pkg) {

        List<PackageItem> itemList = new ArrayList<>();
        for (PackageItemRequest itemRequest : items) {
            Products product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Package not found with ID: " + itemRequest.getProductId()));

            PackageItem item = packageMapper.toPackageItemEntity(itemRequest, product, pkg);

            itemList.add(item);
        }
        return itemList;
    }
}
