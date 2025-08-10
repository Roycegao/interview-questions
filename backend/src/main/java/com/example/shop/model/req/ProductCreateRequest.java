package com.example.shop.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create Product Request
 */
@Data
@ApiModel(value = "ProductCreateRequest", description = "Create product request parameters")
public class ProductCreateRequest {
    /**
     * Product Name
     */
    @NotBlank(message = "Product name cannot be empty")
    @ApiModelProperty(value = "Product Name", example = "iPhone 15 Pro", required = true)
    private String name;
    
    /**
     * Product Price
     */
    @NotNull(message = "Product price cannot be empty")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    @ApiModelProperty(value = "Product Price", example = "8999.00", required = true, notes = "Price must be greater than 0.01")
    private BigDecimal price;
    
    /**
     * Stock Quantity
     */
    @NotNull(message = "Stock quantity cannot be empty")
    @Min(value = 0, message = "Stock quantity cannot be less than 0")
    @ApiModelProperty(value = "Stock Quantity", example = "100", required = true, notes = "Stock quantity cannot be less than 0")
    private Integer quantity;
    
    /**
     * Is Visible
     */
    @ApiModelProperty(value = "Is Visible", example = "true", notes = "Default is true")
    private Boolean visible = true;
} 