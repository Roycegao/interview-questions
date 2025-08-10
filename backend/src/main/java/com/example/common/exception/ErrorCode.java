package com.example.common.exception;

import lombok.Getter;

/**
 * Unified Error Code Enumeration
 */
@Getter
public enum ErrorCode {
    
    // ========== Common Errors (1000-1999) ==========
    SUCCESS(200, "Operation successful"),
    SYSTEM_ERROR(1000, "System error"),
    PARAM_INVALID(1001, "Parameter validation failed"),
    DATA_NOT_FOUND(1002, "Data not found"),
    OPERATION_FAILED(1003, "Operation failed"),
    UNAUTHORIZED(1401, "Unauthorized access"),
    FORBIDDEN(1403, "Access forbidden"),
    
    // ========== Product Related Errors (2000-2999) ==========
    PRODUCT_NOT_FOUND(2001, "Product not found"),
    PRODUCT_NAME_EXISTS(2002, "Product name already exists"),
    PRODUCT_PRICE_INVALID(2003, "Product price invalid"),
    PRODUCT_QUANTITY_INVALID(2004, "Product stock quantity invalid"),
    PRODUCT_INVISIBLE(2005, "Product not visible"),
    
    // ========== Inventory Related Errors (3000-3999) ==========
    INVENTORY_INSUFFICIENT(3001, "Insufficient stock"),
    INVENTORY_QUANTITY_INVALID(3002, "Inventory quantity invalid"),
    INVENTORY_UPDATE_FAILED(3003, "Inventory update failed"),
    
    // ========== Shopping Cart Related Errors (4000-4999) ==========
    CART_NOT_FOUND(4001, "Shopping cart not found"),
    CART_ITEM_NOT_FOUND(4002, "Cart item not found"),
    CART_ITEM_EXISTS(4003, "Product already in cart"),
    CART_QUANTITY_INVALID(4004, "Cart item quantity invalid"),
    CART_EMPTY(4005, "Shopping cart is empty"),
    CART_ITEM_QUANTITY_EXCEED_STOCK(4006, "Cart item quantity exceeds stock"),
    
    // ========== User Related Errors (5000-5999) ==========
    USER_NOT_FOUND(5001, "User not found"),
    USER_SESSION_INVALID(5002, "User session invalid"),
    USER_SESSION_EXPIRED(5003, "User session expired"),
    
    // ========== Database Related Errors (6000-6999) ==========
    DATABASE_ERROR(6001, "Database operation error"),
    DATABASE_CONNECTION_FAILED(6002, "Database connection failed"),
    DATABASE_TRANSACTION_FAILED(6003, "Database transaction failed"),
    
    // ========== Network Related Errors (7000-7999) ==========
    NETWORK_ERROR(7001, "Network error"),
    TIMEOUT_ERROR(7002, "Request timeout"),
    EXTERNAL_SERVICE_ERROR(7003, "External service error");
    
    /**
     * Error Code
     */
    private final Integer code;
    
    /**
     * Error Description
     */
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    /**
     * Get enum by error code
     */
    public static ErrorCode getByCode(Integer code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR;
    }
    
    /**
     * Check if it's a success status
     */
    public boolean isSuccess() {
        return SUCCESS.equals(this);
    }
} 