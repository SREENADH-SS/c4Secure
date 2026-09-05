package com.backend.c4s.Dto.Cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AddToCartRequest {

    @NotNull(message = "productId is Required")
    private Long productId;

    @NotNull(message = "Quantity is Required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
