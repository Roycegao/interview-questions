package com.example.shop.dao;

import com.example.shop.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品DAO接口
 */
@Mapper
public interface ProductDao {
    
    /**
     * 查询产品列表（分页）
     */
    List<Product> selectProducts(@Param("name") String name,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("visible") Boolean visible);

    /**
     * 根据ID查询产品
     */
    Product selectById(@Param("id") Long id);
    
    /**
     * 插入产品
     */
    int insert(Product product);
    
    /**
     * 更新产品
     */
    int update(Product product);
    
    /**
     * 删除产品
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 更新产品可见性
     */
    int updateVisibility(@Param("id") Long id, @Param("visible") Boolean visible);
    
    /**
     * 查询可见产品列表
     */
    List<Product> selectVisibleProducts();
    
    /**
     * 根据ID列表批量查询产品
     */
    List<Product> selectByIds(@Param("ids") List<Long> ids);
    
    /**
     * 批量更新产品
     */
    int batchUpdate(@Param("products") List<Product> products);
    
    /**
     * 根据名称查询产品
     */
    Product selectByName(@Param("name") String name);
} 