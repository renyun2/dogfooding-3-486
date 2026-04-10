package com.student.management.exception;

import org.springframework.http.HttpStatus;

/**
 * 未授权异常
 * 用于认证失败的情况
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED.value(), message);
    }
}
