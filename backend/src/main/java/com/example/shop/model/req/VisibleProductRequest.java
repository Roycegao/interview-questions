package com.example.shop.model.req;

import com.example.common.req.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Query Visible Products Request
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisibleProductRequest extends PageRequest {
    
    /**
     * Product Name (Fuzzy Query)
     */
    private String name;
    
    public VisibleProductRequest() {
        super();
    }
    
    public VisibleProductRequest(Integer pageNum, Integer pageSize) {
        super(pageNum, pageSize);
    }
} 