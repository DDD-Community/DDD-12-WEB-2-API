package com.ddd.api.config.exception;

public class BusinessException extends RuntimeException {

    public BusinessException() {
        super("알 수 없는 오류가 발생했습니다.");
    }

    public BusinessException(String message) {
        super(message);
    }
}
