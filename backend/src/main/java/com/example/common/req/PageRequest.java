package com.example.common.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页请求基类
 */
@Data
@ApiModel(value = "PageRequest", description = "分页请求参数")
public class PageRequest {
    
    /**
     * 页码，从1开始
     */
    @Min(value = 1, message = "页码必须大于0")
    @ApiModelProperty(value = "页码", example = "1", notes = "从1开始")
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @ApiModelProperty(value = "每页大小", example = "10", notes = "最大100")
    private Integer pageSize = 10;
    
    public PageRequest() {}
    
    public PageRequest(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum != null ? pageNum : 1;
        this.pageSize = pageSize != null ? pageSize : 10;
    }
} 