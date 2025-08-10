package com.example.shop.model.req;

import com.example.common.req.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * 产品查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProductQueryRequest", description = "产品查询请求参数")
public class ProductQueryRequest extends PageRequest {
    
    /**
     * 产品名称（模糊查询）
     */
    @ApiModelProperty(value = "产品名称", example = "iPhone", notes = "支持模糊查询")
    private String name;
    
    /**
     * 最低价格
     */
    @DecimalMin(value = "0.00", message = "最低价格不能小于0")
    @ApiModelProperty(value = "最低价格", example = "100.00", notes = "价格范围查询的最低价格")
    private BigDecimal minPrice;
    
    /**
     * 最高价格
     */
    @DecimalMin(value = "0.00", message = "最高价格不能小于0")
    @ApiModelProperty(value = "最高价格", example = "1000.00", notes = "价格范围查询的最高价格")
    private BigDecimal maxPrice;
    
    /**
     * 是否可见
     */
    @ApiModelProperty(value = "是否可见", example = "true", notes = "true=可见，false=不可见，null=查询所有")
    private Boolean visible;
    
    public ProductQueryRequest() {
        super();
    }
    
    public ProductQueryRequest(Integer pageNum, Integer pageSize) {
        super(pageNum, pageSize);
    }
} 