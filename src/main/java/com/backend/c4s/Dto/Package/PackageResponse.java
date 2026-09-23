package com.backend.c4s.Dto.Package;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal packagePrice;

    private BigDecimal installationCharge;

    private BigDecimal originalPrice;

    private BigDecimal savings;

    private String imageUrl;

    private boolean active;

    private List<PackageItemResponse> items;
}
