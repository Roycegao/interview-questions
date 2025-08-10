package com.example.common.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Pagination Request Base Class
 */
@Data
@ApiModel(value = "PageRequest", description = "Pagination Request Parameters")
public class PageRequest {
    
    /**
     * Page Number, starting from 1
     */
    @Min(value = 1, message = "Page number must be greater than 0")
    @ApiModelProperty(value = "Page Number", example = "1", notes = "Starting from 1")
    private Integer pageNum = 1;
    
    /**
     * Page Size
     */
    @Min(value = 1, message = "Page size must be greater than 0")
    @Max(value = 100, message = "Page size cannot exceed 100")
    @ApiModelProperty(value = "Page Size", example = "10", notes = "Maximum 100")
    private Integer pageSize = 10;
    
    public PageRequest() {}
    
    public PageRequest(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum != null ? pageNum : 1;
        this.pageSize = pageSize != null ? pageSize : 10;
    }
} 