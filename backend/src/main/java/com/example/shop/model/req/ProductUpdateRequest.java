package com.example.shop.model.req;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 更新产品请求
 */
@Data
public class ProductUpdateRequest {
    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    private String name;
    
    /**
     * 产品价格
     */
    @NotNull(message = "产品价格不能为空")
    @DecimalMin(value = "0.01", message = "产品价格必须大于0")
    private BigDecimal price;
    
    /**
     * 库存数量
     */
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能小于0")
    private Integer quantity;
    
    /**
     * 是否可见
     */
    private Boolean visible = true;
} 