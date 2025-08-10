package com.example.shop.service;

import com.example.shop.model.entity.Cart;

/**
 * Cart Service Interface
 */
public interface CartService {
    
    /**
     * Get Cart
     */
    Cart getCart(Long userId);
    
    /**
     * Add Product to Cart
     */
    void addToCart(Long userId, Long productId, Integer quantity);
    
    /**
     * Update Cart Item Quantity
     */
    void updateCartItem(Long userId, Long itemId, Integer quantity);
    
    /**
     * Remove Product from Cart
     */
    void removeFromCart(Long userId, Long itemId);
    
    /**
     * Clear Cart
     */
    void clearCart(Long userId);
    
    /**
     * Get Cart Item Count
     */
    Integer getCartItemCount(Long userId);
    
    /**
     * Calculate Cart Total
     */
    java.math.BigDecimal getCartTotal(Long userId);
} 