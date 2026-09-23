package com.backend.c4s.Service;

import com.backend.c4s.Dto.Package.PackageRequest;
import com.backend.c4s.Dto.Package.PackageResponse;

import java.util.List;

public interface PackageService {

    PackageResponse createPackage(PackageRequest request);

    PackageResponse getPackageById(Long id);

    List<PackageResponse>getAllPackages();

    List<PackageResponse>getActivePackages();

    PackageResponse updatePackage(Long id, PackageRequest request);

    PackageResponse togglePackageStatus(Long id);

    void deletePackage(Long id);
}
