package com.example.shop.model.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Update Product Visibility Request
 */
@Data
public class ProductVisibilityUpdateRequest {
    /**
     * Whether visible
     */
    @NotNull(message = "Visibility status cannot be empty")

    private Boolean visible;
} 