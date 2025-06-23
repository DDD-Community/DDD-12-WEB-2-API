package com.moyorak.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyorak.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    private final AuthService authService;

    private final ObjectMapper objectMapper;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService oAuth2UserService)
            throws Exception {
        http.authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                                "/error",
                                                "/h2-console/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/api/auth/refresh",
                                                "/api/user/sign-up")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .headers(
                        headers ->
                                headers.frameOptions(
                                                HeadersConfigurer.FrameOptionsConfig
                                                        ::disable) // H2 콘솔 iframe 허용하기 위함
                                        .contentTypeOptions(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .oauth2Login(
                        oauth ->
                                oauth.userInfoEndpoint(
                                                userInfo -> userInfo.userService(oAuth2UserService))
                                        .successHandler(customOAuth2SuccessHandler)
                                        .failureHandler(customOAuth2FailureHandler))
                .exceptionHandling(
                        ex ->
                                ex.authenticationEntryPoint(authenticationEntryPoint)
                                        .accessDeniedHandler(
                                                new CustomAccessDeniedHandler(objectMapper)))
                .addFilterBefore(
                        new JwtAuthenticationFilter(
                                jwtTokenProvider, authenticationEntryPoint, authService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** 필터 처리에서 제외 될 정적 리소스를 작성합니다. */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
                web.ignoring()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/favicon.ico",
                                "/h2-console/**");
    }
}
