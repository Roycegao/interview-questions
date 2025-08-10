package com.example.shop.service.impl;

import com.example.common.exception.BusinessException;
import com.example.common.exception.ErrorCode;
import com.example.shop.dao.ProductDao;
import com.example.common.resp.PageResponse;
import com.example.shop.model.entity.Product;
import com.example.shop.model.req.ProductQueryRequest;
import com.example.shop.model.req.VisibleProductRequest;
import com.example.shop.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

/**
 * Product Service Implementation Class
 */
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductDao productDao;
    
    @Override
    public PageResponse<Product> getProducts(ProductQueryRequest request) {
        return createPageResponse(
            () -> productDao.selectProducts(
                request.getName(), 
                request.getMinPrice(), 
                request.getMaxPrice(), 
                request.getVisible()
            ),
            request.getPageNum(),
            request.getPageSize()
        );
    }
    
    @Override
    public Product getProductById(Long id) {
        return productDao.selectById(id);
    }
    
    @Override
    @Transactional
    public void createProduct(Product product) {
        Product product1 = productDao.selectByName(product.getName());
        if (null != product1){
            throw new BusinessException(ErrorCode.PRODUCT_NAME_EXISTS);
        }
        productDao.insert(product);
    }
    
    @Override
    @Transactional
    public void updateProduct(Long id, Product product) {
        validateProductExists(id);
        product.setId(id);
        productDao.update(product);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        validateProductExists(id);
        productDao.deleteById(id);
    }
    
    @Override
    @Transactional
    public void updateProductVisibility(Long id, Boolean visible) {
        validateProductExists(id);
        productDao.updateVisibility(id, visible);
    }
    
    @Override
    public List<Product> getVisibleProducts() {
        return productDao.selectVisibleProducts();
    }
    
    @Override
    public PageResponse<Product> getVisibleProducts(VisibleProductRequest request) {
        return createPageResponse(
            () -> productDao.selectProducts(
                request.getName(), 
                null, 
                null, 
                true  // Only query visible products
            ),
            request.getPageNum(),
            request.getPageSize()
        );
    }

    /**
     * Create paginated response
     */
    private PageResponse<Product> createPageResponse(ProductQuerySupplier querySupplier, 
                                                   Integer pageNum, 
                                                   Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = querySupplier.get();
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        PageResponse.Pagination pagination = new PageResponse.Pagination(
            pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal()
        );
        
        return new PageResponse<>(products, pagination);
    }
    
    /**
     * Validate if product exists
     */
    private void validateProductExists(Long id) {
        Product existingProduct = productDao.selectById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found, ID: " + id);
        }
    }
    
    /**
     * Validate if all products in the product ID list exist
     */
    private void validateProductIdsExist(List<Long> productIds) {
        List<Product> existingProducts = productDao.selectByIds(productIds);
        if (existingProducts.size() != productIds.size()) {
            List<Long> existingIds = extractProductIds(existingProducts);
            productIds.removeAll(existingIds);
            throw new RuntimeException("The following products do not exist: " + productIds);
        }
    }
    
    /**
     * Extract product ID list
     */
    private List<Long> extractProductIds(List<Product> products) {
        List<Long> ids = new ArrayList<>();
        for (Product product : products) {
            if (product != null && product.getId() != null) {
                ids.add(product.getId());
        }
        }
        return ids;
    }
    
    /**
     * Product query functional interface
     */
    @FunctionalInterface
    private interface ProductQuerySupplier {
        List<Product> get();
    }
} 