package com.moyorak.api.auth.domain;

import com.moyorak.config.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("유효하지 않은 회원 정보입니다.");
    }
}
