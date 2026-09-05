package com.backend.c4s.Dto.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class  ProductImageResponse {

    private Long id;
    private Long productId;
    private String fileName;
    private String imageUrl;
    private String fileType;
    private Long size;
    private LocalDateTime uploadedAt;

}
