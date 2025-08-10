package com.example.shop.dao;

import com.example.shop.model.entity.Cart;
import com.example.shop.model.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Cart DAO Interface
 */
@Mapper
public interface CartDao {
    
    /**
     * Query Cart by User ID
     */
    Cart selectByUserId(@Param("userId") Long userId);
    
    /**
     * Create Cart
     */
    int insertCart(Cart cart);
    
    /**
     * Query Cart Item List
     */
    List<CartItem> selectCartItems(@Param("cartId") Long cartId);
    
    /**
     * Query Cart Item by ID
     */
    CartItem selectCartItemById(@Param("id") Long id);
    
    /**
     * Query Cart Item by Cart ID and Product ID
     */
    CartItem selectCartItemByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
    
    /**
     * Insert Cart Item
     */
    int insertCartItem(CartItem cartItem);
    
    /**
     * Update Cart Item
     */
    int updateCartItem(CartItem cartItem);
    
    /**
     * Delete Cart Item by ID
     */
    int deleteCartItemById(@Param("id") Long id);
    
    /**
     * Delete All Cart Items by Cart ID
     */
    int deleteCartItemsByCartId(@Param("cartId") Long cartId);
    
    /**
     * Query Cart Item Count
     */
    Integer countCartItems(@Param("cartId") Long cartId);
    
    /**
     * Calculate Cart Total Amount
     */
    java.math.BigDecimal sumCartTotal(@Param("cartId") Long cartId);
} 