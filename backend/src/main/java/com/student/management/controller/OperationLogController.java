package com.student.management.controller;

import com.student.management.entity.OperationLog;
import com.student.management.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/operation-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "操作日志管理", description = "系统操作日志的查询和管理接口")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping
    @Operation(summary = "分页查询操作日志")
    public ResponseEntity<Map<String, Object>> getOperationLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String module) {

        Page<OperationLog> logPage = operationLogService.getOperationLogs(page, size, startTime, endTime, operator, module);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", logPage.getContent());
        response.put("total", logPage.getTotalElements());
        response.put("totalPages", logPage.getTotalPages());
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取操作日志详情")
    public ResponseEntity<Map<String, Object>> getOperationLogById(@PathVariable Long id) {
        OperationLog log = operationLogService.getOperationLogById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", log);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志")
    public ResponseEntity<Map<String, Object>> deleteOperationLog(@PathVariable Long id) {
        operationLogService.deleteOperationLog(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}
