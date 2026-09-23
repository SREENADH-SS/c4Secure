package com.backend.c4s.Dto.Package;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
