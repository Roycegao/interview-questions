package com.example.shop.controller;

import com.example.shop.model.entity.Cart;
import com.example.shop.model.entity.CartItem;
import com.example.shop.model.req.CartItemAddRequest;
import com.example.shop.model.req.CartItemUpdateRequest;
import com.example.shop.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.notNullValue;

/**
 * CartController 单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("购物车控制器单元测试")
class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private ObjectMapper objectMapper;
    private Cart testCart;
    private CartItem testCartItem;
    private MockHttpSession mockSession;

    @BeforeEach
    void setUp() {
        // 手动构建MockMvc，不依赖Spring上下文
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
        
        // 准备测试购物车项
        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setCartId(1L);
        testCartItem.setProductId(1L);
        testCartItem.setProductName("测试商品");
        testCartItem.setPrice(new BigDecimal("99.99"));
        testCartItem.setQuantity(2);
        testCartItem.setTotalPrice(new BigDecimal("199.98"));

        // 准备测试购物车
        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUserId(123456L);
        testCart.setItems(Arrays.asList(testCartItem));
        testCart.setTotalAmount(new BigDecimal("199.98"));
        testCart.setItemCount(2);
        testCart.setCreatedAt(LocalDateTime.now());
        testCart.setUpdatedAt(LocalDateTime.now());

        // 准备Mock Session
        mockSession = new MockHttpSession();
        mockSession.setAttribute("userId", "test-user-123");
    }

    @Test
    @DisplayName("获取购物车 - 成功")
    void testGetCart_Success() throws Exception {
        // 准备测试数据
        when(cartService.getCart(anyLong())).thenReturn(testCart);

        // 执行测试并验证
        mockMvc.perform(get("/api/cart")
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.userId").value(123456))
                .andExpect(jsonPath("$.data.totalAmount").value(199.98))
                .andExpect(jsonPath("$.data.itemCount").value(2))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items[0].productName").value("测试商品"));
    }

    @Test
    @DisplayName("添加商品到购物车 - 成功")
    void testAddToCart_Success() throws Exception {
        // 准备测试数据
        CartItemAddRequest request = new CartItemAddRequest();
        request.setProductId(1L);
        request.setQuantity(3);

        // CartService.addToCart is void method, no need to mock return value

        // 执行测试并验证
        mockMvc.perform(post("/api/cart/items")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Product added to cart successfully"));
    }

    @Test
    @DisplayName("添加商品到购物车 - 参数校验失败")
    void testAddToCart_ValidationFailed() throws Exception {
        // 准备测试数据（缺少必填字段）
        CartItemAddRequest request = new CartItemAddRequest();
        // 不设置productId，触发校验失败

        // 执行测试并验证
        mockMvc.perform(post("/api/cart/items")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("更新购物车商品数量 - 成功")
    void testUpdateCartItem_Success() throws Exception {
        // 准备测试数据
        CartItemUpdateRequest request = new CartItemUpdateRequest();
        request.setQuantity(5);

        // CartService.updateCartItem is void method, no need to mock return value

        // 执行测试并验证
        mockMvc.perform(put("/api/cart/items/{itemId}", 1L)
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cart item updated successfully"));
    }

    @Test
    @DisplayName("删除购物车商品 - 成功")
    void testRemoveFromCart_Success() throws Exception {
        // 准备测试数据
        // CartService.removeFromCart is void method, no need to mock return value

        // 执行测试并验证
        mockMvc.perform(delete("/api/cart/items/{itemId}", 1L)
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product removed from cart successfully"));
    }

    @Test
    @DisplayName("清空购物车 - 成功")
    void testClearCart_Success() throws Exception {
        // 执行测试并验证
        mockMvc.perform(delete("/api/cart")
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Shopping cart cleared successfully"));
    }

    @Test
    @DisplayName("获取购物车商品数量 - 成功")
    void testGetCartItemCount_Success() throws Exception {
        // 准备测试数据
        when(cartService.getCartItemCount(anyLong())).thenReturn(5);

        // 执行测试并验证
        mockMvc.perform(get("/api/cart/count")
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(5));
    }

    @Test
    @DisplayName("计算购物车总价 - 成功")
    void testGetCartTotal_Success() throws Exception {
        // 准备测试数据
        BigDecimal total = new BigDecimal("199.98");
        when(cartService.getCartTotal(anyLong())).thenReturn(total);

        // 执行测试并验证
        mockMvc.perform(get("/api/cart/total")
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(199.98));
    }

    @Test
    @DisplayName("Session管理 - 新用户自动创建Session")
    void testSessionManagement_NewUser() throws Exception {
        // 使用新的空Session
        MockHttpSession newSession = new MockHttpSession();

        when(cartService.getCart(anyLong())).thenReturn(testCart);

        // 执行测试并验证
        mockMvc.perform(get("/api/cart")
                        .session(newSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().string("X-User-ID", notNullValue())); 
    }
} 