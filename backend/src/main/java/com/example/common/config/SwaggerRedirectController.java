package com.example.common.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Knife4j UI Redirect Controller
 * Provides redirect support for multiple access paths
 */
@Controller
public class SwaggerRedirectController {

    /**
     * Redirect to Knife4j main page
     */
    @GetMapping("/swagger-ui.html")
    public String redirectToSwaggerUi() {
        return "redirect:/doc.html";
    }
    
    /**
     * Redirect to Knife4j main page
     */
    @GetMapping("/swagger")
    public String redirectToSwagger() {
        return "redirect:/doc.html";
    }
    
    /**
     * Redirect to Knife4j main page
     */
    @GetMapping("/api-docs")
    public String redirectToApiDocs() {
        return "redirect:/doc.html";
    }
} 