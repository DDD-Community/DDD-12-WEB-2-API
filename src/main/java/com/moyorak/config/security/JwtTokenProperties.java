package com.moyorak.config.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
class JwtTokenProperties {

    private final String secretKey;

    private final String refreshSecretKey;

    JwtTokenProperties(
            @Value("${jwt.secret}") final String secretKey,
            @Value("${jwt.refresh-secret}") final String refreshSecretKey) {
        this.secretKey = secretKey;
        this.refreshSecretKey = refreshSecretKey;
    }
}
