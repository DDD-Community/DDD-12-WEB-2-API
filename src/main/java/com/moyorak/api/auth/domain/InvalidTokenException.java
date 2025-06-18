package com.moyorak.api.auth.domain;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidTokenException extends BadCredentialsException {

    public InvalidTokenException() {
        super("유효하지 않은 로그인 정보입니다.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
