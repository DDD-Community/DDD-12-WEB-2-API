package com.moyorak.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                                "/error",
                                                "/h2-console/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .headers(
                        headers ->
                                headers.frameOptions(
                                                HeadersConfigurer.FrameOptionsConfig
                                                        ::disable) // H2 콘솔 iframe 허용하기 위함
                                        .contentTypeOptions(Customizer.withDefaults()))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }
}
