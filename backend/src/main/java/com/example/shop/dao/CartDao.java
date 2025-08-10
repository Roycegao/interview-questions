package com.example.shop.dao;

import com.example.shop.model.entity.Cart;
import com.example.shop.model.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车DAO接口
 */
@Mapper
public interface CartDao {
    
    /**
     * 根据用户ID查询购物车
     */
    Cart selectByUserId(@Param("userId") Long userId);
    
    /**
     * 创建购物车
     */
    int insertCart(Cart cart);
    
    /**
     * 查询购物车项目列表
     */
    List<CartItem> selectCartItems(@Param("cartId") Long cartId);
    
    /**
     * 根据ID查询购物车项目
     */
    CartItem selectCartItemById(@Param("id") Long id);
    
    /**
     * 根据购物车ID和产品ID查询购物车项目
     */
    CartItem selectCartItemByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
    
    /**
     * 插入购物车项目
     */
    int insertCartItem(CartItem cartItem);
    
    /**
     * 更新购物车项目
     */
    int updateCartItem(CartItem cartItem);
    
    /**
     * 删除购物车项目
     */
    int deleteCartItemById(@Param("id") Long id);
    
    /**
     * 删除购物车所有项目
     */
    int deleteCartItemsByCartId(@Param("cartId") Long cartId);
    
    /**
     * 查询购物车项目数量
     */
    Integer countCartItems(@Param("cartId") Long cartId);
    
    /**
     * 计算购物车总金额
     */
    java.math.BigDecimal sumCartTotal(@Param("cartId") Long cartId);
} 