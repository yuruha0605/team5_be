package com.example.team5_be.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

//swagger에서 token을 test 해보고 싶을 때도
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customerOpenAPI() {
        // Security Schema 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization") ;
        
                //Security Requiremenmt
                SecurityRequirement securityRequirement = new SecurityRequirement()
                    .addList("BearerAuth");

        return new OpenAPI()
                    .addSecurityItem(securityRequirement)
                    .schemaRequirement("BearerAuth", securityScheme);
        
    }
}
