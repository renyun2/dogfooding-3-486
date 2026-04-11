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

    @Column(length = 50)
    private String operator;

    @Column(nullable = false, length = 100)
    private String module;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 20)
    private String method;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String params;

    @Column(length = 50)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "operation_time", updatable = false)
    private LocalDateTime operationTime;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Boolean success;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
