package com.moyorak.api.infra.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
