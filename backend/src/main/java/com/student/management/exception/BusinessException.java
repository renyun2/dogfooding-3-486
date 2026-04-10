package com.student.management.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常
 * 用于业务规则校验失败的情况，如：邮箱已存在、数据冲突等
 */
public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public BusinessException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST.value(), message, cause);
    }
}
