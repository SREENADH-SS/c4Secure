package com.backend.c4s.Dto.Brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BrandResponse {

    private Long id;
    private String name;
    private String logoUrl;
}
