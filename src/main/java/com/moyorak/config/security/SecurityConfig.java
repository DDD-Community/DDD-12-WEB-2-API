package com.moyorak.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
class SecurityConfig {

    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService oAuth2UserService)
            throws Exception {
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
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .oauth2Login(
                        oauth ->
                                oauth.userInfoEndpoint(
                                                userInfo -> userInfo.userService(oAuth2UserService))
                                        .successHandler(customOAuth2SuccessHandler)
                                        .failureHandler(customOAuth2FailureHandler));

        return http.build();
    }
}
