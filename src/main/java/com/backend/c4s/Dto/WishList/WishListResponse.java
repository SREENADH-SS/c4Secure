package com.backend.c4s.Dto.WishList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class WishListResponse {
    private Long id;
    private Long userId;
    private Set<WishListProducts> products;
}
