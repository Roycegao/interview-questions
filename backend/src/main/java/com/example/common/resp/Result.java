package com.example.common.resp;

import com.example.common.exception.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Unified API Response Result Class
 */
@Data
@ApiModel(value = "Result", description = "Unified API Response Result")
public class Result<T> {
    /**
     * Error Code
     */
    @ApiModelProperty(value = "Response Status Code", example = "200", notes = "200=Success, Others=Failure")
    private Integer code;
    
    /**
     * Is Success
     */
    @ApiModelProperty(value = "Is Success", example = "true")
    private boolean success;
    
    /**
     * Response Data
     */
    @ApiModelProperty(value = "Response Data")
    private T data;
    
    /**
     * Response Message
     */
    @ApiModelProperty(value = "Response Message", example = "Operation successful")
    private String message;
    
    /**
     * Error Information
     */
    @ApiModelProperty(value = "Detailed Error Information", example = "Parameter validation failed")
    private String error;
    
    public Result() {
    }
    
    public Result(ErrorCode errorCode, T data, String message) {
        this.code = errorCode.getCode();
        this.success = errorCode.isSuccess();
        this.data = data;
        this.message = message;
    }
    
    /**
     * Success Response
     */
    public static <T> Result<T> success(T data) {
        Result<T> response = new Result<>();
        response.setCode(ErrorCode.SUCCESS.getCode());
        response.setSuccess(true);
        response.setData(data);
        response.setMessage(ErrorCode.SUCCESS.getMessage());
        return response;
    }
    
    /**
     * Success Response (Custom Message)
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> response = new Result<>();
        response.setCode(ErrorCode.SUCCESS.getCode());
        response.setSuccess(true);
        response.setData(data);
        response.setMessage(message);
        return response;
    }
    
    /**
     * Error Response
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        Result<T> response = new Result<>();
        response.setCode(errorCode.getCode());
        response.setSuccess(false);
        response.setMessage(errorCode.getMessage());
        response.setError(errorCode.getMessage());
        return response;
    }
    
    /**
     * Error Response (Custom Error Information)
     */
    public static <T> Result<T> error(ErrorCode errorCode, String errorDetails) {
        Result<T> response = new Result<>();
        response.setCode(errorCode.getCode());
        response.setSuccess(false);
        response.setMessage(errorCode.getMessage());
        response.setError(errorDetails);
        return response;
    }
    

    @Deprecated
    public static <T> Result<T> error(String error) {
        Result<T> response = new Result<>();
        response.setCode(ErrorCode.SYSTEM_ERROR.getCode());
        response.setSuccess(false);
        response.setMessage(ErrorCode.SYSTEM_ERROR.getMessage());
        response.setError(error);
        return response;
    }
} 