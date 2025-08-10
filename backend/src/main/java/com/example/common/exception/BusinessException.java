package com.example.common.exception;

import lombok.Getter;

/**
 * Business Exception Class
 */
@Getter
public class BusinessException extends RuntimeException {
    
    /**
     * Error Code
     */
    private final ErrorCode errorCode;
    
    /**
     * Error Details
     */
    private final String details;
    
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }
    
    public BusinessException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage() + (details != null ? ": " + details : ""));
        this.errorCode = errorCode;
        this.details = details;
    }
    
    public BusinessException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getMessage() + (details != null ? ": " + details : ""), cause);
        this.errorCode = errorCode;
        this.details = details;
    }
    
    /**
     * Get Error Code
     */
    public Integer getCode() {
        return errorCode.getCode();
    }
    
    /**
     * Get Error Message
     */
    public String getErrorMessage() {
        return errorCode.getMessage();
    }
    
    /**
     * Get Full Error Message
     */
    public String getFullMessage() {
        return getMessage();
    }
} 