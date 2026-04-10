package com.student.management.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源不存在异常
 * 用于根据ID查询不到资源的情况
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(HttpStatus.NOT_FOUND.value(), String.format("%s不存在，ID: %d", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String field, Object value) {
        super(HttpStatus.NOT_FOUND.value(), String.format("%s不存在，%s: %s", resourceName, field, value));
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
