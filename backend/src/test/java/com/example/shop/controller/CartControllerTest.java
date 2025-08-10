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
 * CartController Unit Test
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Cart Controller Unit Test")
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
        // Manually build MockMvc, don't depend on Spring context
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
        
        // Prepare test cart item
        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setCartId(1L);
        testCartItem.setProductId(1L);
        testCartItem.setProductName("Test Product");
        testCartItem.setPrice(new BigDecimal("99.99"));
        testCartItem.setQuantity(2);
        testCartItem.setTotalPrice(new BigDecimal("199.98"));

        // Prepare test cart
        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUserId(123456L);
        testCart.setItems(Arrays.asList(testCartItem));
        testCart.setTotalAmount(new BigDecimal("199.98"));
        testCart.setItemCount(2);
        testCart.setCreatedAt(LocalDateTime.now());
        testCart.setUpdatedAt(LocalDateTime.now());

        // Prepare Mock Session
        mockSession = new MockHttpSession();
        mockSession.setAttribute("userId", "test-user-123");
    }

    @Test
    @DisplayName("Get Cart - Success")
    void testGetCart_Success() throws Exception {
        // Prepare test data
        when(cartService.getCart(anyLong())).thenReturn(testCart);

        // Execute test and verify
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
                .andExpect(jsonPath("$.data.items[0].productName").value("Test Product"));
    }

    @Test
    @DisplayName("Add Product to Cart - Success")
    void testAddToCart_Success() throws Exception {
        // Prepare test data
        CartItemAddRequest request = new CartItemAddRequest();
        request.setProductId(1L);
        request.setQuantity(3);

        // CartService.addToCart is void method, no need to mock return value

        // Execute test and verify
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
    @DisplayName("Add Product to Cart - Parameter Validation Failed")
    void testAddToCart_ValidationFailed() throws Exception {
        // Prepare test data (missing required fields)
        CartItemAddRequest request = new CartItemAddRequest();
        // Do not set productId to trigger validation failure

        // Execute test and verify
        mockMvc.perform(post("/api/cart/items")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Cart Item Quantity - Success")
    void testUpdateCartItem_Success() throws Exception {
        // Prepare test data
        CartItemUpdateRequest request = new CartItemUpdateRequest();
        request.setQuantity(5);

        // CartService.updateCartItem is void method, no need to mock return value

        // Execute test and verify
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
    @DisplayName("Remove Cart Item - Success")
    void testRemoveFromCart_Success() throws Exception {
        // Prepare test data
        // CartService.removeFromCart is void method, no need to mock return value

        // Execute test and verify
        mockMvc.perform(delete("/api/cart/items/{itemId}", 1L)
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product removed from cart successfully"));
    }

    @Test
    @DisplayName("Clear Cart - Success")
    void testClearCart_Success() throws Exception {
        // Execute test and verify
        mockMvc.perform(delete("/api/cart")
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Shopping cart cleared successfully"));
    }

    @Test
    @DisplayName("Get Cart Item Count - Success")
    void testGetCartItemCount_Success() throws Exception {
        // Prepare test data
        when(cartService.getCartItemCount(anyLong())).thenReturn(5);

        // Execute test and verify
        mockMvc.perform(get("/api/cart/count")
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(5));
    }

    @Test
    @DisplayName("Calculate Cart Total - Success")
    void testGetCartTotal_Success() throws Exception {
        // Prepare test data
        BigDecimal total = new BigDecimal("199.98");
        when(cartService.getCartTotal(anyLong())).thenReturn(total);

        // Execute test and verify
        mockMvc.perform(get("/api/cart/total")
                        .session(mockSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(199.98));
    }

    @Test
    @DisplayName("Session Management - New User Automatically Creates Session")
    void testSessionManagement_NewUser() throws Exception {
        // Use a new empty session
        MockHttpSession newSession = new MockHttpSession();

        when(cartService.getCart(anyLong())).thenReturn(testCart);

        // Execute test and verify
        mockMvc.perform(get("/api/cart")
                        .session(newSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().string("X-User-ID", notNullValue())); 
    }
} 