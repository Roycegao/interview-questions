package com.example.shop.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Add Cart Item Request
 */
@Data
@ApiModel(value = "CartItemAddRequest", description = "Add cart item request parameters")
public class CartItemAddRequest {
    /**
     * Product ID
     */
    @NotNull(message = "Product ID cannot be empty")
    @ApiModelProperty(value = "Product ID", example = "1", required = true)
    private Long productId;
    
    /**
     * Quantity
     */
    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @ApiModelProperty(value = "Product Quantity", example = "2", required = true, notes = "Quantity must be greater than 0")
    private Integer quantity;
} 