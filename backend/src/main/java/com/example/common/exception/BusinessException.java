package com.example.common.exception;

import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {
    
    /**
     * 错误码
     */
    private final ErrorCode errorCode;
    
    /**
     * 错误详细信息
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
     * 获取错误码
     */
    public Integer getCode() {
        return errorCode.getCode();
    }
    
    /**
     * 获取错误消息
     */
    public String getErrorMessage() {
        return errorCode.getMessage();
    }
    
    /**
     * 获取完整错误信息
     */
    public String getFullMessage() {
        return getMessage();
    }
} 