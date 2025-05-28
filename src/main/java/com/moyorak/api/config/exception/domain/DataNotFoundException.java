package com.moyorak.api.config.exception.domain;

import com.moyorak.api.config.exception.BusinessException;

public class DataNotFoundException extends BusinessException {

    public DataNotFoundException() {
        super("존재하지 않는 데이터입니다.");
    }
}
