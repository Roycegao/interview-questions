package com.example.shop.dao;

import com.example.shop.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product DAO Interface
 */
@Mapper
public interface ProductDao {
    
    /**
     * Query Product List (Pagination)
     */
    List<Product> selectProducts(@Param("name") String name,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("visible") Boolean visible);

    /**
     * Query Product by ID
     */
    Product selectById(@Param("id") Long id);
    
    /**
     * Insert Product
     */
    int insert(Product product);
    
    /**
     * Update Product
     */
    int update(Product product);
    
    /**
     * Delete Product by ID
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * Update Product Visibility
     */
    int updateVisibility(@Param("id") Long id, @Param("visible") Boolean visible);
    
    /**
     * Query Visible Product List
     */
    List<Product> selectVisibleProducts();
    
    /**
     * Batch Query Products by ID List
     */
    List<Product> selectByIds(@Param("ids") List<Long> ids);
    
    /**
     * Batch Update Products
     */
    int batchUpdate(@Param("products") List<Product> products);
    
    /**
     * Query Product by Name
     */
    Product selectByName(@Param("name") String name);
} 