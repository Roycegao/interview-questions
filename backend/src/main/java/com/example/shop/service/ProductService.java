package com.example.shop.service;

import com.example.common.resp.PageResponse;
import com.example.shop.model.entity.Product;
import com.example.shop.model.req.ProductQueryRequest;
import com.example.shop.model.req.VisibleProductRequest;

import java.util.List;

/**
 * Product Service Interface
 */
public interface ProductService {
    
    /**
     * Paginated product list query
     */
    PageResponse<Product> getProducts(ProductQueryRequest request);
    
    /**
     * Query product by ID
     */
    Product getProductById(Long id);
    
    /**
     * Create product
     */
    void createProduct(Product product);
    
    /**
     * Update product
     */
    void updateProduct(Long id, Product product);
    
    /**
     * Delete product
     */
    void deleteProduct(Long id);
    
    /**
     * Update product visibility
     */
    void updateProductVisibility(Long id, Boolean visible);
    
    /**
     * Query visible product list (simple version, no pagination)
     */
    List<Product> getVisibleProducts();
    
    /**
     * Paginated visible product list query
     */
    PageResponse<Product> getVisibleProducts(VisibleProductRequest request);
} 