package com.example.shop.service;

import com.example.shop.model.entity.Cart;

/**
 * 购物车Service接口
 */
public interface CartService {
    
    /**
     * 获取购物车
     */
    Cart getCart(Long userId);
    
    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, Long productId, Integer quantity);
    
    /**
     * 更新购物车商品数量
     */
    void updateCartItem(Long userId, Long itemId, Integer quantity);
    
    /**
     * 删除购物车商品
     */
    void removeFromCart(Long userId, Long itemId);
    
    /**
     * 清空购物车
     */
    void clearCart(Long userId);
    
    /**
     * 获取购物车商品数量
     */
    Integer getCartItemCount(Long userId);
    
    /**
     * 计算购物车总价
     */
    java.math.BigDecimal getCartTotal(Long userId);
} 