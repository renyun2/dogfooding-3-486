package com.student.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation_logs")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operator", length = 100)
    private String operator;

    @Column(name = "module", length = 50)
    private String module;

    @Column(name = "operation_type", length = 50)
    private String operationType;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "request_url", length = 255)
    private String requestUrl;

    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "operation_time")
    private LocalDateTime operationTime;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "is_success")
    private Boolean isSuccess;

    @Column(name = "error_msg", columnDefinition = "TEXT")
    private String errorMsg;
}
