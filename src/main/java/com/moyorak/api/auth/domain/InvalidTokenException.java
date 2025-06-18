package com.moyorak.api.auth.domain;

import com.moyorak.config.exception.BusinessException;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super("유효하지 않은 로그인 정보입니다.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
