package com.moyorak.config.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
class JwtTokenProperties {

    private final String secretKey;

    JwtTokenProperties(@Value("${jwt.secret}") final String secretKey) {
        this.secretKey = secretKey;
    }
}
