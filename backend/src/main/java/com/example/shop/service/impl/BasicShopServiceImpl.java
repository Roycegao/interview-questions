package com.example.shop.service.impl;

import com.example.shop.dao.BasicShopDao;
import com.example.shop.model.BasicShop;
import com.example.shop.service.BasicShopService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author gaort
 */
@Service
public class BasicShopServiceImpl implements BasicShopService {

    @Resource
    private BasicShopDao basicShopDao;

    @Override
    public PageInfo<BasicShop> query(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BasicShop> basicShops = null;
        PageInfo result = new PageInfo(basicShops);
        return result;
    }
}
