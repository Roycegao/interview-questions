package com.example.common.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Knife4j UI 重定向控制器
 * 提供多种访问路径的重定向支持
 */
@Controller
public class SwaggerRedirectController {

    /**
     * 重定向到 Knife4j 主页面
     */
    @GetMapping("/swagger-ui.html")
    public String redirectToSwaggerUi() {
        return "redirect:/doc.html";
    }
    
    /**
     * 重定向到 Knife4j 主页面
     */
    @GetMapping("/swagger")
    public String redirectToSwagger() {
        return "redirect:/doc.html";
    }
    
    /**
     * 重定向到 Knife4j 主页面
     */
    @GetMapping("/api-docs")
    public String redirectToApiDocs() {
        return "redirect:/doc.html";
    }
} 