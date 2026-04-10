package com.student.management.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源重复异常
 * 用于唯一性校验失败的情况，如：邮箱已存在、名称重复等
 */
public class DuplicateResourceException extends BaseException {

    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(HttpStatus.CONFLICT.value(), String.format("%s已存在，%s: %s", resourceName, field, value));
    }

    public DuplicateResourceException(String message) {
        super(HttpStatus.CONFLICT.value(), message);
    }
}
