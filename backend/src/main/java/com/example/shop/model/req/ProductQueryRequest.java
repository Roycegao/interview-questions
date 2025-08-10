package com.example.shop.model.req;

import com.example.common.req.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * Product Query Request
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProductQueryRequest", description = "Product Query Request Parameters")
public class ProductQueryRequest extends PageRequest {
    
    /**
     * Product Name (Fuzzy Query)
     */
    @ApiModelProperty(value = "Product Name", example = "iPhone", notes = "Supports fuzzy query")
    private String name;
    
    /**
     * Minimum Price
     */
    @DecimalMin(value = "0.00", message = "Minimum price cannot be less than 0")
    @ApiModelProperty(value = "Minimum Price", example = "100.00", notes = "Minimum price for price range query")
    private BigDecimal minPrice;
    
    /**
     * Maximum Price
     */
    @DecimalMin(value = "0.00", message = "Maximum price cannot be less than 0")
    @ApiModelProperty(value = "Maximum Price", example = "1000.00", notes = "Maximum price for price range query")
    private BigDecimal maxPrice;
    
    /**
     * Whether visible
     */
    @ApiModelProperty(value = "Whether Visible", example = "true", notes = "true=visible, false=invisible, null=query all")
    private Boolean visible;
    
    public ProductQueryRequest() {
        super();
    }
    
    public ProductQueryRequest(Integer pageNum, Integer pageSize) {
        super(pageNum, pageSize);
    }
} 