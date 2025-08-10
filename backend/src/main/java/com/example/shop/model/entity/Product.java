package com.example.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product Entity Class
 */
@Data
@ApiModel(value = "Product", description = "Product Information")
public class Product {
    /**
     * Product ID
     */
    @ApiModelProperty(value = "Product ID", example = "1")
    private Long id;
    
    /**
     * Product Name
     */
    @ApiModelProperty(value = "Product Name", example = "iPhone 15 Pro")
    private String name;
    
    /**
     * Product Price
     */
    @ApiModelProperty(value = "Product Price", example = "8999.00")
    private BigDecimal price;
    
    /**
     * Stock Quantity
     */
    @ApiModelProperty(value = "Stock Quantity", example = "100")
    private Integer quantity;
    
    /**
     * Is Visible
     */
    @ApiModelProperty(value = "Is Visible", example = "true")
    private Boolean visible;
    
    /**
     * Creation Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "Creation Time", example = "2024-01-01 12:00:00")
    private LocalDateTime createdAt;
    
    /**
     * Update Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "Update Time", example = "2024-01-01 12:00:00")
    private LocalDateTime updatedAt;
} 