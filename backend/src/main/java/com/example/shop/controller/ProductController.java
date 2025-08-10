package com.example.shop.controller;

import com.example.common.resp.Result;
import com.example.common.resp.PageResponse;
import com.example.shop.model.entity.Product;
import com.example.shop.model.req.ProductQueryRequest;
import com.example.shop.model.req.VisibleProductRequest;
import com.example.shop.model.req.ProductCreateRequest;
import com.example.shop.model.req.ProductUpdateRequest;
import com.example.shop.model.req.ProductVisibilityUpdateRequest;
import com.example.shop.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Product Management Controller
 */
@Api(tags = "Product Management", description = "Product CRUD operations and related APIs")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Paginated product list query
     */
    @ApiOperation(value = "Paginated Product List Query", notes = "Paginated query with filtering by name and visibility")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Query successful"),
        @ApiResponse(code = 1001, message = "Parameter validation failed")
    })
    @GetMapping
    public Result<PageResponse<Product>> getProducts(
            @ApiParam(value = "Product query conditions", required = true) @Valid ProductQueryRequest request) {
        PageResponse<Product> result = productService.getProducts(request);
        return Result.success(result);
    }

    /**
     * Query product details by ID
     */
    @ApiOperation(value = "Query Product Details", notes = "Get detailed information by product ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Query successful"),
        @ApiResponse(code = 2001, message = "Product not found")
    })
    @GetMapping("/{id}")
    public Result<Product> getProduct(
            @ApiParam(value = "Product ID", required = true, example = "1") @PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return Result.error("Product not found");
        }
        return Result.success(product);
    }
    
    /**
     * Create product
     */
    @ApiOperation(value = "Create Product", notes = "Create new product information")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Created successfully"),
        @ApiResponse(code = 1001, message = "Parameter validation failed"),
        @ApiResponse(code = 2002, message = "Product name already exists")
    })
    @PostMapping
    public Result<Void> createProduct(
            @ApiParam(value = "Product information", required = true) @Valid @RequestBody ProductCreateRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        productService.createProduct(product);
        return Result.success(null, "Product created successfully");
    }
    
    /**
     * Update product
     */
    @ApiOperation(value = "Update Product", notes = "Update product information by specified ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Updated successfully"),
        @ApiResponse(code = 1001, message = "Parameter validation failed"),
        @ApiResponse(code = 2001, message = "Product not found")
    })
    @PutMapping("/{id}")
    public Result<Void> updateProduct(
            @ApiParam(value = "Product ID", required = true, example = "1") @PathVariable Long id, 
            @ApiParam(value = "Product information", required = true) @Valid @RequestBody ProductUpdateRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        productService.updateProduct(id, product);
        return Result.success(null, "Product updated successfully");
    }
    
    /**
     * Delete product
     */
    @ApiOperation(value = "Delete Product", notes = "Delete product by specified ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Deleted successfully"),
        @ApiResponse(code = 2001, message = "Product not found")
    })
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(
            @ApiParam(value = "Product ID", required = true, example = "1") @PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success(null, "Product deleted successfully");
    }
    
    /**
     * Update product visibility
     */
    @ApiOperation(value = "Update Product Visibility", notes = "Quickly toggle product show/hide status")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Updated successfully"),
        @ApiResponse(code = 1001, message = "Parameter validation failed"),
        @ApiResponse(code = 2001, message = "Product not found")
    })
    @PatchMapping("/{id}/visibility")
    public Result<Void> updateProductVisibility(
            @ApiParam(value = "Product ID", required = true, example = "1") @PathVariable Long id, 
            @ApiParam(value = "Visibility settings", required = true) @Valid @RequestBody ProductVisibilityUpdateRequest request) {
        productService.updateProductVisibility(id, request.getVisible());
        return Result.success(null, "Product visibility updated successfully");
    }
    
    /**
     * Query visible product list (simple version, no pagination)
     */
    @ApiOperation(value = "Query Visible Product List", notes = "Get simple list of all visible products, no pagination")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Query successful")
    })
    @GetMapping("/visible/simple")
    public Result<List<Product>> getVisibleProducts() {
        List<Product> products = productService.getVisibleProducts();
        return Result.success(products);
    }
    
    /**
     * Paginated visible product list query
     */
    @ApiOperation(value = "Paginated Visible Product Query", notes = "Get paginated visible product list with name filtering support")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Query successful"),
        @ApiResponse(code = 1001, message = "Parameter validation failed")
    })
    @GetMapping("/visible")
    public Result<PageResponse<Product>> getVisibleProductsPaged(
            @ApiParam(value = "Query conditions", required = true) @Valid VisibleProductRequest request) {
        PageResponse<Product> result = productService.getVisibleProducts(request);
        return Result.success(result);
    }
} 