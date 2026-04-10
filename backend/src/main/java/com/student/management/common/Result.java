package com.student.management.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * 统一API响应封装类
 *
 * @param <T> 响应数据类型
 */
@Data
@Schema(description = "统一响应对象")
public class Result<T> {

    @Schema(description = "状态码", example = "200")
    private Integer code;

    @Schema(description = "响应消息", example = "操作成功")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应时间戳")
    private LocalDateTime timestamp;

    public Result() {
        this.timestamp = LocalDateTime.now();
    }

    public Result(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ==================== 成功响应 ====================

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.OK.value(), "操作成功", null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.OK.value(), "操作成功", data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(HttpStatus.OK.value(), message, data);
    }

    /**
     * 成功响应（无数据，带消息）
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> success(String message) {
        return (Result<T>) new Result<Void>(HttpStatus.OK.value(), message, null);
    }

    /**
     * 创建成功响应（201）
     */
    public static <T> Result<T> created(T data) {
        return new Result<>(HttpStatus.CREATED.value(), "创建成功", data);
    }

    // ==================== 失败响应 ====================

    /**
     * 失败响应
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> fail(String message) {
        return (Result<T>) new Result<Void>(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    /**
     * 失败响应（带状态码）
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> fail(Integer code, String message) {
        return (Result<T>) new Result<Void>(code, message, null);
    }

    /**
     * 资源不存在
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> notFound(String message) {
        return (Result<T>) new Result<Void>(HttpStatus.NOT_FOUND.value(), message, null);
    }

    /**
     * 未授权
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> unauthorized(String message) {
        return (Result<T>) new Result<Void>(HttpStatus.UNAUTHORIZED.value(), message, null);
    }

    /**
     * 服务器内部错误
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> error(String message) {
        return (Result<T>) new Result<Void>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    // ==================== 转换为ResponseEntity ====================

    /**
     * 转换为ResponseEntity（HTTP 200）
     */
    public ResponseEntity<Result<T>> toResponseEntity() {
        return ResponseEntity.status(code).body(this);
    }

    /**
     * 转换为ResponseEntity（自定义HTTP状态）
     */
    public ResponseEntity<Result<T>> toResponseEntity(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(this);
    }

    // ==================== 便捷工厂方法 ====================

    /**
     * 根据条件返回成功或失败
     */
    public static <T> Result<T> of(boolean success, String successMsg, String failMsg, T data) {
        return success ? success(successMsg, data) : fail(failMsg);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code != null && code >= 200 && code < 300;
    }
}
