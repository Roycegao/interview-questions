package com.example.shop.service;

import com.example.common.exception.BusinessException;
import com.example.common.exception.ErrorCode;
import com.example.shop.dao.CartDao;
import com.example.shop.dao.ProductDao;
import com.example.shop.model.entity.Cart;
import com.example.shop.model.entity.CartItem;
import com.example.shop.model.entity.Product;
import com.example.shop.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cart Service Test")
class CartServiceTest {

    @Mock
    private CartDao cartDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart testCart;
    private Product testProduct;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setQuantity(100);
        testProduct.setVisible(true);

        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setCartId(1L);
        testCartItem.setProductId(1L);
        testCartItem.setProductName("Test Product");
        testCartItem.setPrice(new BigDecimal("99.99"));
        testCartItem.setQuantity(2);
        testCartItem.setTotalPrice(new BigDecimal("199.98"));

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUserId(1L);
        testCart.setItems(Arrays.asList(testCartItem));
        testCart.setTotalAmount(new BigDecimal("199.98"));
        testCart.setItemCount(2);
        testCart.setCreatedAt(LocalDateTime.now());
        testCart.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Get Cart - Cart Exists")
    void testGetCart_CartExists() {
        // Given
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.sumCartTotal(1L)).thenReturn(new BigDecimal("199.98"));
        when(cartDao.countCartItems(1L)).thenReturn(2);

        // When
        Cart result = cartService.getCart(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(new BigDecimal("199.98"), result.getTotalAmount());
        assertEquals(2, result.getItemCount());

        verify(cartDao).selectByUserId(1L);
        // calculateCartTotals will call these two methods
        verify(cartDao).sumCartTotal(1L);
        verify(cartDao).countCartItems(1L);
    }

    @Test
    @DisplayName("Get Cart - Cart Not Exists, Auto Create")
    void testGetCart_CartNotExists_CreateNew() {
        // Given
        Cart newCart = new Cart();
        newCart.setId(2L);
        newCart.setUserId(1L);

        when(cartDao.selectByUserId(1L)).thenReturn(null).thenReturn(newCart);
        when(cartDao.insertCart(any(Cart.class))).thenReturn(1);

        // When
        Cart result = cartService.getCart(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());

        verify(cartDao, atLeastOnce()).selectByUserId(1L);
        verify(cartDao).insertCart(any(Cart.class));
    }

    @Test
    @DisplayName("Add Product to Cart - Successfully Add New Product")
    void testAddToCart_NewProduct_Success() {
        // Given
        when(productDao.selectById(1L)).thenReturn(testProduct);
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.selectCartItemByCartIdAndProductId(1L, 1L)).thenReturn(null);
        when(cartDao.insertCartItem(any(CartItem.class))).thenReturn(1);

        // When
        cartService.addToCart(1L, 1L, 2);

        // Then
        verify(productDao).selectById(1L);
        verify(cartDao, atLeastOnce()).selectByUserId(1L);
        verify(cartDao).selectCartItemByCartIdAndProductId(1L, 1L);
        verify(cartDao).insertCartItem(any(CartItem.class));
    }

    @Test
    @DisplayName("Add Product to Cart - Product Not Found")
    void testAddToCart_ProductNotFound() {
        // Given
        when(productDao.selectById(999L)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.addToCart(1L, 999L, 2);
        });

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
        verify(productDao).selectById(999L);
    }

    @Test
    @DisplayName("Add Product to Cart - Product Invisible")
    void testAddToCart_ProductInvisible() {
        // Given
        testProduct.setVisible(false);
        when(productDao.selectById(1L)).thenReturn(testProduct);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.addToCart(1L, 1L, 1);
        });

        assertEquals(ErrorCode.PRODUCT_INVISIBLE, exception.getErrorCode());
        verify(productDao).selectById(1L);
    }

    @Test
    @DisplayName("Add Product to Cart - Insufficient Stock")
    void testAddToCart_InsufficientStock() {
        // Given
        testProduct.setQuantity(1);
        when(productDao.selectById(1L)).thenReturn(testProduct);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.addToCart(1L, 1L, 2);
        });

        assertEquals(ErrorCode.INVENTORY_INSUFFICIENT, exception.getErrorCode());
        verify(productDao).selectById(1L);
    }

    @Test
    @DisplayName("Add Product to Cart - Update Existing Item Quantity")
    void testAddToCart_UpdateExistingItem_Success() {
        // Given
        testProduct.setQuantity(10); // Ensure sufficient stock
        when(productDao.selectById(1L)).thenReturn(testProduct);
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.selectCartItemByCartIdAndProductId(1L, 1L)).thenReturn(testCartItem);
        when(productDao.update(any(Product.class))).thenReturn(1);

        // When
        cartService.addToCart(1L, 1L, 1);

        // Then
        verify(cartDao).selectCartItemByCartIdAndProductId(1L, 1L);
        verify(productDao).update(any(Product.class)); // Verify inventory deduction
        verify(cartDao).updateCartItem(any(CartItem.class));
        verify(cartDao, atLeastOnce()).selectByUserId(1L);
    }

    @Test
    @DisplayName("Update Cart Item Quantity - Success")
    void testUpdateCartItem_Success() {
        // Given
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.selectCartItemById(1L)).thenReturn(testCartItem);
        when(productDao.selectById(1L)).thenReturn(testProduct); // Need product info to update inventory
        when(productDao.update(any(Product.class))).thenReturn(1);

        // When
        cartService.updateCartItem(1L, 1L, 3);

        // Then
        verify(cartDao).selectCartItemById(1L);
        verify(productDao).selectById(1L); // Verify get product info
        verify(productDao).update(any(Product.class)); // Verify inventory update
        verify(cartDao).updateCartItem(any(CartItem.class));
    }

    @Test
    @DisplayName("Update Cart Item Quantity - Item Not Found")
    void testUpdateCartItem_ItemNotFound() {
        // Given
        when(cartDao.selectCartItemById(999L)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.updateCartItem(1L, 999L, 3);
        });

        assertEquals(ErrorCode.CART_ITEM_NOT_FOUND, exception.getErrorCode());
        verify(cartDao).selectCartItemById(999L);
        verify(cartDao, never()).updateCartItem(any());
    }

    @Test
    @DisplayName("Remove Product from Cart - Success")
    void testRemoveFromCart_Success() {
        // Given
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.selectCartItemById(1L)).thenReturn(testCartItem);
        when(productDao.selectById(1L)).thenReturn(testProduct);
        when(productDao.update(any(Product.class))).thenReturn(1);

        // When
        cartService.removeFromCart(1L, 1L);

        // Then
        verify(cartDao).selectCartItemById(1L);
        verify(productDao).selectById(1L); // Verify get product info
        verify(productDao).update(any(Product.class)); // Verify inventory restoration
        verify(cartDao).deleteCartItemById(1L);
    }

    @Test
    @DisplayName("Clear Cart - Success")
    void testClearCart_Success() {
        // Given
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.selectCartItems(1L)).thenReturn(cartItems);
        when(productDao.selectByIds(Arrays.asList(1L))).thenReturn(Arrays.asList(testProduct));
        when(productDao.batchUpdate(anyList())).thenReturn(1);

        // When
        cartService.clearCart(1L);

        // Then
        verify(cartDao).selectCartItems(1L);
        verify(productDao).selectByIds(Arrays.asList(1L)); // Verify batch get product info
        verify(productDao).batchUpdate(anyList()); // Verify batch update inventory
        verify(cartDao).deleteCartItemsByCartId(1L);
    }

    @Test
    @DisplayName("Get Cart Item Count - Success")
    void testGetCartItemCount_Success() {
        // Given
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.sumCartTotal(1L)).thenReturn(new BigDecimal("199.98"));
        when(cartDao.countCartItems(1L)).thenReturn(5);

        // When
        Integer result = cartService.getCartItemCount(1L);

        // Then
        assertEquals(5, result);
        verify(cartDao).selectByUserId(1L);
        // countCartItems will be called twice: once in getCart's calculateCartTotals, once in getCartItemCount
        verify(cartDao, times(2)).countCartItems(1L);
    }

    @Test
    @DisplayName("Calculate Cart Total - Success")
    void testGetCartTotal_Success() {
        // Given
        BigDecimal expectedTotal = new BigDecimal("199.98");
        when(cartDao.selectByUserId(1L)).thenReturn(testCart);
        when(cartDao.sumCartTotal(1L)).thenReturn(expectedTotal);
        when(cartDao.countCartItems(1L)).thenReturn(2);

        // When
        BigDecimal result = cartService.getCartTotal(1L);

        // Then
        assertEquals(expectedTotal, result);
        verify(cartDao).selectByUserId(1L);
        // sumCartTotal will be called twice: once in getCart's calculateCartTotals, once in getCartTotal
        verify(cartDao, times(2)).sumCartTotal(1L);
    }
} 