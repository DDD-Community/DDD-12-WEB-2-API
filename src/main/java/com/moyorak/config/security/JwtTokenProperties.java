package com.moyorak.config.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
class JwtTokenProperties {

    private final String secretKey;

    private final Long expiration;

    private final String refreshSecretKey;

    private final Long refreshExpiration;

    JwtTokenProperties(
            @Value("${jwt.secret}") final String secretKey,
            @Value("${jwt.refresh-secret}") final String refreshSecretKey,
            @Value("${jwt.expiration}") final Long expiration,
            @Value("${jwt.refresh-expiration}") final Long refreshExpiration) {
        this.secretKey = secretKey;
        this.refreshSecretKey = refreshSecretKey;
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }
}
