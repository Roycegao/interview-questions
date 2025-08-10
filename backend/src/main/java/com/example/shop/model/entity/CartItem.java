package com.example.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车项目实体类
 */
@Data
public class CartItem {
    /**
     * 购物车项目ID
     */
    private Long id;
    
    /**
     * 购物车ID
     */
    private Long cartId;
    
    /**
     * 产品ID
     */
    private Long productId;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 产品价格
     */
    private BigDecimal price;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 总价
     */
    private BigDecimal totalPrice;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
} 