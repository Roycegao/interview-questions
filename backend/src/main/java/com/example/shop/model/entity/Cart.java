package com.example.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Shopping Cart Entity Class
 */
@Data
@ApiModel(value = "Cart", description = "Shopping Cart Information")
public class Cart {
    /**
     * Cart ID
     */
    @ApiModelProperty(value = "Cart ID", example = "1")
    private Long id;
    
    /**
     * User ID
     */
    @ApiModelProperty(value = "User ID", example = "123456")
    private Long userId;
    
    /**
     * Cart Item List
     */
    @ApiModelProperty(value = "Shopping Cart Item List")
    private List<CartItem> items;
    
    /**
     * Total Amount
     */
    @ApiModelProperty(value = "Cart Total Amount", example = "999.99")
    private BigDecimal totalAmount;
    
    /**
     * Item Count
     */
    @ApiModelProperty(value = "Total Item Count", example = "5")
    private Integer itemCount;
    
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