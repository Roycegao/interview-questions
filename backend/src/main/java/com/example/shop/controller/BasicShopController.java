package com.example.shop.controller;

import com.example.shop.service.BasicShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author gaort
 */
@Controller
@RequestMapping(value = "/basic/shop")
public class BasicShopController {

    @Resource
    private BasicShopService basicShopService;


    @ResponseBody
    @GetMapping("/query")
    public Object findAllUser(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    int pageSize){
        return null;
    }
}
