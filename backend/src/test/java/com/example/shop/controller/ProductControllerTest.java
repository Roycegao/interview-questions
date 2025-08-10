package com.example.shop.controller;

import com.example.common.resp.Result;
import com.example.common.resp.PageResponse;
import com.example.shop.model.entity.Product;
import com.example.shop.model.req.ProductCreateRequest;
import com.example.shop.model.req.ProductUpdateRequest;
import com.example.shop.model.req.ProductQueryRequest;
import com.example.shop.model.req.ProductVisibilityUpdateRequest;
import com.example.shop.model.req.VisibleProductRequest;
import com.example.shop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProductController Unit Test
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Controller Unit Test")
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setQuantity(100);
        testProduct.setVisible(true);
        testProduct.setCreatedAt(LocalDateTime.now());
        testProduct.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Get Product Details - Success")
    void testGetProduct_Success() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(get("/api/products/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.price").value(99.99));
    }

    @Test
    @DisplayName("Get Product Details - Product Not Found")
    void testGetProduct_NotFound() throws Exception {
        // Given
        when(productService.getProductById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/products/{id}", 999L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(1000));
    }

    @Test
    @DisplayName("Get Visible Products List - Success")
    void testGetVisibleProducts_Success() throws Exception {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getVisibleProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/visible/simple"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Test Product"));
    }

    @Test
    @DisplayName("Pagination Query Products - Success")
    void testGetProducts_Success() throws Exception {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        PageResponse.Pagination pagination = new PageResponse.Pagination(1, 10, 1L);
        PageResponse<Product> pageResponse = new PageResponse<>(products, pagination);
        
        when(productService.getProducts(any(ProductQueryRequest.class))).thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/api/products")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.pagination.pageNum").value(1));
    }

    @Test
    @DisplayName("Create Product - Success")
    void testCreateProduct_Success() throws Exception {
        // Given
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("New Product");
        request.setPrice(new BigDecimal("199.99"));
        request.setQuantity(50);
        request.setVisible(true);

        // ProductService.createProduct is void method, no need to mock return value

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"));
    }

    @Test
    @DisplayName("Create Product - Parameter Validation Failed")
    void testCreateProduct_ValidationFailed() throws Exception {
        // Given
        ProductCreateRequest request = new ProductCreateRequest();
        // Don't set required fields, trigger validation failure

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Product - Success")
    void testUpdateProduct_Success() throws Exception {
        // Given
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Updated Product");
        request.setPrice(new BigDecimal("299.99"));
        request.setQuantity(75);
        request.setVisible(true);

        // ProductService.updateProduct is void method, no need to mock return value

        // When & Then
        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product updated successfully"));
    }

    @Test
    @DisplayName("Delete Product - Success")
    void testDeleteProduct_Success() throws Exception {
        // Given
        // ProductService.deleteProduct is a void method, no need to mock return value

        // When & Then
        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    @DisplayName("Update Product Visibility - Success")
    void testUpdateProductVisibility_Success() throws Exception {
        // Given
        ProductVisibilityUpdateRequest request = new ProductVisibilityUpdateRequest();
        request.setVisible(false);

        // ProductService.updateProductVisibility is void method, no need to mock return value

        // When & Then
        mockMvc.perform(patch("/api/products/{id}/visibility", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product visibility updated successfully"));
    }
} 