package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Package.PackageRequest;
import com.backend.c4s.Dto.Package.PackageResponse;
import com.backend.c4s.Service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
@Tag(name ="package catalog",description ="An endpoint for the packages and offers for the users")
public class PackageController {

    private final PackageService packageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "to create a package")
    public ResponseEntity<PackageResponse>createPackage(@Valid @ModelAttribute PackageRequest request){
        PackageResponse response= packageService.createPackage(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "get active package for the public")
    public ResponseEntity<List<PackageResponse>>getAllPackage(
            @RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly
    ){
        List<PackageResponse>packages= activeOnly? packageService.getActivePackages()
                : packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get package by id")
    public ResponseEntity<PackageResponse> getPackageById(@PathVariable Long id){
        return ResponseEntity.ok(packageService.getPackageById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "to update existing package")
    public ResponseEntity<PackageResponse>updatePackage(@PathVariable Long id,
                                                        @Valid @ModelAttribute PackageRequest request){
        return ResponseEntity.ok(packageService.updatePackage(id,request));
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "to update status of the package")
    public ResponseEntity<PackageResponse>toggleStatus(@PathVariable Long id){
        return ResponseEntity.ok(packageService.togglePackageStatus(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "to delete package")
    public ResponseEntity<Void>deletePackage(@PathVariable Long id){
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}
