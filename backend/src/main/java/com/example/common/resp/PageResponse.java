package com.example.common.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Pagination Response Class
 */
@Data
@ApiModel(value = "PageResponse", description = "Pagination Response Result")
public class PageResponse<T> {
    /**
     * Data List
     */
    @ApiModelProperty(value = "Data List")
    private List<T> list;
    
    /**
     * Pagination Information
     */
    @ApiModelProperty(value = "Pagination Information")
    private Pagination pagination;
    
    public PageResponse() {
    }
    
    public PageResponse(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }
    
    /**
     * Pagination Information Class
     */
    @Data
    @ApiModel(value = "Pagination", description = "Pagination Information")
    public static class Pagination {
        /**
         * Current Page Number
         */
        @ApiModelProperty(value = "Current Page Number", example = "1")
        private Integer pageNum;
        
        /**
         * Page Size
         */
        @ApiModelProperty(value = "Page Size", example = "10")
        private Integer pageSize;
        
        /**
         * Total Record Count
         */
        @ApiModelProperty(value = "Total Record Count", example = "100")
        private Long total;
        
        /**
         * Total Page Count
         */
        @ApiModelProperty(value = "Total Page Count", example = "10")
        private Integer totalPages;
        
        public Pagination() {
        }
        
        public Pagination(Integer pageNum, Integer pageSize, Long total) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.total = total;
            this.totalPages = (int) Math.ceil((double) total / pageSize);
        }
    }
} 