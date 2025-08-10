package com.example.common.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页响应类
 */
@Data
@ApiModel(value = "PageResponse", description = "分页响应结果")
public class PageResponse<T> {
    /**
     * 数据列表
     */
    @ApiModelProperty(value = "数据列表")
    private List<T> list;
    
    /**
     * 分页信息
     */
    @ApiModelProperty(value = "分页信息")
    private Pagination pagination;
    
    public PageResponse() {
    }
    
    public PageResponse(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }
    
    /**
     * 分页信息类
     */
    @Data
    @ApiModel(value = "Pagination", description = "分页信息")
    public static class Pagination {
        /**
         * 当前页码
         */
        @ApiModelProperty(value = "当前页码", example = "1")
        private Integer pageNum;
        
        /**
         * 每页大小
         */
        @ApiModelProperty(value = "每页大小", example = "10")
        private Integer pageSize;
        
        /**
         * 总记录数
         */
        @ApiModelProperty(value = "总记录数", example = "100")
        private Long total;
        
        /**
         * 总页数
         */
        @ApiModelProperty(value = "总页数", example = "10")
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