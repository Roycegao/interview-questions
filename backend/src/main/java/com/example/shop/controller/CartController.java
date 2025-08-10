package com.example.shop.controller;

import com.example.common.resp.Result;
import com.example.shop.model.entity.Cart;
import com.example.shop.model.req.CartItemAddRequest;
import com.example.shop.model.req.CartItemUpdateRequest;
import com.example.shop.service.CartService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Shopping Cart Management Controller
 */
@Api(tags = "Shopping Cart Management", description = "Shopping cart CRUD operations and related APIs")
@RestController
@RequestMapping("/api/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Get current user ID
     * Priority: use client-provided userId, generate new one if not available
     */
    private Long getCurrentUserId(HttpServletRequest request, HttpServletResponse response) {
        // 1. First try to get user ID from request header
        String clientUserId = request.getHeader("X-User-ID");
        log.info("=== User ID Processing ===");
        log.info("Request header X-User-ID: {}", clientUserId);
        
        if (clientUserId != null && !clientUserId.isEmpty()) {
            try {
                Long userId = Long.parseLong(clientUserId);
                log.info("✅ Using client user ID: {}", userId);
                // Even when using existing ID, return it in response header to ensure frontend can read it
                response.setHeader("X-User-ID", userId.toString());
                return userId;
            } catch (NumberFormatException e) {
                log.warn("❌ Client user ID format invalid: {}, Error: {}", clientUserId, e.getMessage());
            }
        }
        
        // 2. If no valid client user ID, generate new one
        Long userId = generateNewUserId();
        log.warn("Generated new user ID: {} (Reason: client didn't provide valid ID)", userId);
        
        // 3. Tell client to save this user ID via response header
        response.setHeader("X-User-ID", userId.toString());
        log.info("Set response header X-User-ID: {}", userId);
        
        return userId;
    }
    
    /**
     * Generate new user ID
     */
    private Long generateNewUserId() {
        // Use current timestamp as user ID (simple and effective)
        return System.currentTimeMillis();
    }

    /**
     * Get shopping cart details
     */
    @ApiOperation(value = "Get Shopping Cart", notes = "Get detailed shopping cart information for current user")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retrieved successfully"),
        @ApiResponse(code = 1000, message = "System error")
    })
    @GetMapping
    public Result<Cart> getCart(HttpServletRequest request, HttpServletResponse response) {
        Long userId = getCurrentUserId(request, response);
        Cart cart = cartService.getCart(userId);
        return Result.success(cart);
    }

    /**
     * Add product to shopping cart
     */
    @ApiOperation(value = "Add Product to Cart", notes = "Add specified product to current user's shopping cart")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Added successfully"),
        @ApiResponse(code = 2001, message = "Product not found"),
        @ApiResponse(code = 2002, message = "Product not visible"),
        @ApiResponse(code = 3001, message = "Insufficient stock")
    })
    @PostMapping("/items")
    public Result<Void> addToCart(@ApiParam(value = "Add to cart request", required = true)
                                 @Valid @RequestBody CartItemAddRequest request,
                                 HttpServletRequest httpRequest, 
                                 HttpServletResponse httpResponse) {
        Long userId = getCurrentUserId(httpRequest, httpResponse);
        cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return Result.success(null, "Product added to cart successfully");
    }

    /**
     * Update cart item quantity
     */
    @ApiOperation(value = "Update Cart Item Quantity", notes = "Update quantity of specified item in shopping cart")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Updated successfully"),
        @ApiResponse(code = 3002, message = "Cart item not found"),
        @ApiResponse(code = 3001, message = "Insufficient stock")
    })
    @PutMapping("/items/{itemId}")
    public Result<Void> updateCartItem(@ApiParam(value = "Cart item ID", required = true, example = "1") 
                                      @PathVariable Long itemId,
                                      @ApiParam(value = "Update cart item request", required = true)
                                      @Valid @RequestBody CartItemUpdateRequest request,
                                      HttpServletRequest httpRequest, 
                                      HttpServletResponse httpResponse) {
        Long userId = getCurrentUserId(httpRequest, httpResponse);
        cartService.updateCartItem(userId, itemId, request.getQuantity());
        return Result.success(null, "Cart item updated successfully");
    }

    /**
     * Remove product from cart
     */
    @ApiOperation(value = "Remove Product from Cart", notes = "Remove specified product from shopping cart")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Removed successfully"),
        @ApiResponse(code = 3002, message = "Cart item not found")
    })
    @DeleteMapping("/items/{itemId}")
    public Result<Void> removeFromCart(@ApiParam(value = "Cart item ID", required = true, example = "1") 
                                      @PathVariable Long itemId,
                                      HttpServletRequest request, 
                                      HttpServletResponse response) {
        Long userId = getCurrentUserId(request, response);
        cartService.removeFromCart(userId, itemId);
        return Result.success(null, "Product removed from cart successfully");
    }

    /**
     * Clear shopping cart
     */
    @ApiOperation(value = "Clear Shopping Cart", notes = "Remove all products from current user's shopping cart")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Cleared successfully"),
        @ApiResponse(code = 1000, message = "System error")
    })
    @DeleteMapping
    public Result<Void> clearCart(HttpServletRequest request, HttpServletResponse response) {
        Long userId = getCurrentUserId(request, response);
        cartService.clearCart(userId);
        return Result.success(null, "Shopping cart cleared successfully");
    }

    /**
     * Get cart item count
     */
    @ApiOperation(value = "Get Cart Item Count", notes = "Get total number of items in current user's shopping cart")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Query successful"),
        @ApiResponse(code = 1000, message = "System error")
    })
    @GetMapping("/count")
    public Result<Integer> getCartItemCount(HttpServletRequest request, HttpServletResponse response) {
        Long userId = getCurrentUserId(request, response);
        Integer count = cartService.getCartItemCount(userId);
        return Result.success(count);
    }

    /**
     * Get cart total amount
     */
    @ApiOperation(value = "Get Cart Total Amount", notes = "Get total amount of all products in current user's shopping cart")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Query successful"),
        @ApiResponse(code = 1000, message = "System error")
    })
    @GetMapping("/total")
    public Result<BigDecimal> getCartTotal(HttpServletRequest request, HttpServletResponse response) {
        Long userId = getCurrentUserId(request, response);
        BigDecimal total = cartService.getCartTotal(userId);
        return Result.success(total);
    }
} 