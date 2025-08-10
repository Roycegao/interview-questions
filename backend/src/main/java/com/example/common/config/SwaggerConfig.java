package com.example.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Collections;
import java.util.List;

/**
 * Knife4j（增强版 Swagger UI）配置类
 */
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.shop.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("🛍️ Basic Shop API 文档")
                .description("## 基础商城系统 RESTful API 接口文档\n\n" +
                        "### 功能模块\n" +
                        "- 🏪 **商品管理**: 商品的增删改查、库存管理\n" +
                        "- 🛒 **购物车管理**: 购物车商品的添加、修改、删除\n" +
                        "- 👤 **用户会话**: 基于 Session 的用户识别\n\n" +
                        "### 技术栈\n" +
                        "- **后端**: Spring Boot 2.7.x + MyBatis + MySQL\n" +
                        "- **前端**: React + Redux + TypeScript + Ant Design\n" +
                        "- **文档**: Knife4j (增强版 Swagger UI)")
                .version("1.0.0")
                .contact(new Contact("Basic Shop Team", "https://github.com/example/basic-shop", "shop@example.com"))
                .license("Apache License 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .termsOfServiceUrl("https://example.com/terms")
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(
                new ApiKey("Session", "JSESSIONID", "cookie")
        );
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(operationContext -> true)
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "访问所有接口");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("Session", authorizationScopes)
        );
    }
} 