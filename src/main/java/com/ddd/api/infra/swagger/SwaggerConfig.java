package com.ddd.api.infra.swagger;

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
                                .title("DDD 12기 WEB 2팀 API")
                                .description("DDD 12기 WEB 2팀 API SWAGGER UI입니다."));
    }
}
