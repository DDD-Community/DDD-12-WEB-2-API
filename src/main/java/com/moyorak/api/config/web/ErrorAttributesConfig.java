package com.moyorak.api.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ErrorAttributesConfig {

    @Bean
    public CustomErrorAttributes defaultErrorAttributes() {
        return new CustomErrorAttributes();
    }
}
