package com.example.shop.model.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Update Cart Item Quantity Request
 */
@Data
public class CartItemUpdateRequest {
    /**
     * Quantity
     */
    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
} 