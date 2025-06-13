package com.moyorak.infra.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
class SwaggerConfig {

    @Bean
    public OpenAPI apiConfig() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("모여락 API")
                                .description("DDD 12기 WEB 2팀 모여락 API SWAGGER UI입니다."));
    }
}
