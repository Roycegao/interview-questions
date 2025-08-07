package com.example.shop.service;

import com.example.shop.model.BasicShop;
import com.github.pagehelper.PageInfo;

/**
 * @author gaort
 */
public interface BasicShopService {

    PageInfo<BasicShop> query(int pageNum, int pageSize);
}
