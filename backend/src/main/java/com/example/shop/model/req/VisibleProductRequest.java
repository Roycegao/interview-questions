package com.example.shop.model.req;

import com.example.common.req.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询可见产品请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisibleProductRequest extends PageRequest {
    
    /**
     * 产品名称（模糊查询）
     */
    private String name;
    
    public VisibleProductRequest() {
        super();
    }
    
    public VisibleProductRequest(Integer pageNum, Integer pageSize) {
        super(pageNum, pageSize);
    }
} 