package com.example.shop.model.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新产品可见性请求
 */
@Data
public class ProductVisibilityUpdateRequest {
    /**
     * 是否可见
     */
    @NotNull(message = "可见性状态不能为空")
    private Boolean visible;
} 