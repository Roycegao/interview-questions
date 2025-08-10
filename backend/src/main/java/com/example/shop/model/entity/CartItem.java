package com.example.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cart Item Entity Class
 */
@Data
public class CartItem {
    /**
     * Cart Item ID
     */
    private Long id;
    
    /**
     * Cart ID
     */
    private Long cartId;
    
    /**
     * Product ID
     */
    private Long productId;
    
    /**
     * Product Name
     */
    private String productName;
    
    /**
     * Product Price
     */
    private BigDecimal price;
    
    /**
     * Quantity
     */
    private Integer quantity;
    
    /**
     * Total Price
     */
    private BigDecimal totalPrice;
    
    /**
     * Created Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * Updated Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
} 