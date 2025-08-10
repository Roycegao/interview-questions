package com.example.shop.model.req;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Update Product Request
 */
@Data
public class ProductUpdateRequest {
    /**
     * Product Name
     */
    @NotBlank(message = "Product name cannot be empty")
    private String name;
    
    /**
     * Product Price
     */
    @NotNull(message = "Product price cannot be empty")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    private BigDecimal price;
    
    /**
     * Inventory Quantity
     */
    @NotNull(message = "Inventory quantity cannot be empty")
    @Min(value = 0, message = "Inventory quantity cannot be less than 0")
    private Integer quantity;
    
    /**
     * Whether visible
     */
    private Boolean visible = true;
} 