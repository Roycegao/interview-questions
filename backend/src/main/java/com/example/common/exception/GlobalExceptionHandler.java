package com.example.common.exception;

import com.example.common.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * Global Exception Handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Business Exception Handler
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("Business exception: {}", e.getMessage(), e);
        return Result.error(e.getErrorCode(), e.getDetails());
    }
    
    /**
     * Parameter Validation Exception Handler - @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Parameter validation exception: {}", errorMessage);
        return Result.error(ErrorCode.PARAM_INVALID, errorMessage);
    }
    
    /**
     * Parameter Validation Exception Handler - @Validated
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String errorMessage = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Parameter binding exception: {}", errorMessage);
        return Result.error(ErrorCode.PARAM_INVALID, errorMessage);
    }
    
    /**
     * Constraint Validation Exception Handler
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("Constraint validation exception: {}", errorMessage);
        return Result.error(ErrorCode.PARAM_INVALID, errorMessage);
    }
    
    /**
     * Illegal Argument Exception Handler
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument exception: {}", e.getMessage(), e);
        return Result.error(ErrorCode.PARAM_INVALID, e.getMessage());
    }
    
    /**
     * Null Pointer Exception Handler
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        log.error("Null pointer exception", e);
        return Result.error(ErrorCode.SYSTEM_ERROR, "System internal error");
    }
    
    /**
     * General Runtime Exception Handler
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception: {}", e.getMessage(), e);
        return Result.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
    
    /**
     * General Exception Handler
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("Unknown exception: {}", e.getMessage(), e);
        return Result.error(ErrorCode.SYSTEM_ERROR, "System exception, please contact administrator");
    }
} 