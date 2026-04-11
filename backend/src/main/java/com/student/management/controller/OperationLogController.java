package com.student.management.controller;

import com.student.management.entity.OperationLog;
import com.student.management.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operation-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "操作日志", description = "系统操作日志审计接口")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping
    @Operation(summary = "分页查询操作日志", description = "支持按时间范围、操作人、模块筛选")
    public ResponseEntity<Map<String, Object>> getOperationLogs(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "操作人") @RequestParam(required = false) String operator,
            @Parameter(description = "操作模块") @RequestParam(required = false) String module,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {

        Page<OperationLog> logs = operationLogService.getOperationLogs(startTime, endTime, operator, module, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", logs.getContent());
        response.put("total", logs.getTotalElements());
        response.put("page", logs.getNumber());
        response.put("size", logs.getSize());
        response.put("totalPages", logs.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    @Operation(summary = "获取最近的操作日志")
    public ResponseEntity<Map<String, Object>> getRecentLogs() {
        List<OperationLog> logs = operationLogService.getRecentLogs();

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", logs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取操作日志详情")
    public ResponseEntity<Map<String, Object>> getLogById(@PathVariable Long id) {
        OperationLog log = operationLogService.getLogById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", log);

        return ResponseEntity.ok(response);
    }
}
