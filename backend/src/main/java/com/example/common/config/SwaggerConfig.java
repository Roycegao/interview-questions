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
 * Knife4j (Enhanced Swagger UI) Configuration Class
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
                .title("🛍️ Basic Shop API Documentation")
                .description("## Basic Shop System RESTful API Interface Documentation\n\n" +
                        "### Functional Modules\n" +
                        "- 🏪 **Product Management**: Product CRUD operations, inventory management\n" +
                        "- 🛒 **Shopping Cart Management**: Add, modify, delete items in shopping cart\n" +
                        "- 👤 **User Session**: Session-based user identification\n\n" +
                        "### Technology Stack\n" +
                        "- **Backend**: Spring Boot 2.7.x + MyBatis + MySQL\n" +
                        "- **Frontend**: React + Redux + TypeScript + Ant Design\n" +
                        "- **Documentation**: Knife4j (Enhanced Swagger UI)")
                .version("1.0.0")
                .contact(new Contact("Basic Shop Team", "https://github.com/Roycegao/interview-questions", "shop@example.com"))
                .license("Apache License 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .termsOfServiceUrl("")
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
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "Access all interfaces");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("Session", authorizationScopes)
        );
    }
} 