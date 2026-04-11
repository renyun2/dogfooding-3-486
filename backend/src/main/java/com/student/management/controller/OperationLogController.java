package com.student.management.controller;

import com.student.management.annotation.OperationLog;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operation-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "操作日志管理", description = "操作日志查询接口")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping
    @Operation(summary = "分页查询操作日志")
    @OperationLog(module = "操作日志", operationType = "查询", description = "分页查询操作日志列表")
    public ResponseEntity<Map<String, Object>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<com.student.management.entity.OperationLog> logs = operationLogService.getLogs(page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", Map.of(
            "content", logs.getContent(),
            "totalElements", logs.getTotalElements(),
            "totalPages", logs.getTotalPages(),
            "currentPage", logs.getNumber(),
            "size", logs.getSize()
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取操作日志详情")
    @OperationLog(module = "操作日志", operationType = "查询", description = "查询操作日志详情")
    public ResponseEntity<Map<String, Object>> getLogById(@PathVariable Long id) {
        com.student.management.entity.OperationLog log = operationLogService.getLogById(id);
        Map<String, Object> response = new HashMap<>();
        if (log != null) {
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", log);
        } else {
            response.put("code", 404);
            response.put("message", "日志不存在");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "条件搜索操作日志")
    @OperationLog(module = "操作日志", operationType = "查询", description = "条件搜索操作日志")
    public ResponseEntity<Map<String, Object>> searchLogs(
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) Boolean isSuccess,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<com.student.management.entity.OperationLog> logs = operationLogService.searchLogs(
            operator, module, startTime, endTime, isSuccess, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", Map.of(
            "content", logs.getContent(),
            "totalElements", logs.getTotalElements(),
            "totalPages", logs.getTotalPages(),
            "currentPage", logs.getNumber(),
            "size", logs.getSize()
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/modules")
    @Operation(summary = "获取所有操作模块")
    @OperationLog(module = "操作日志", operationType = "查询", description = "获取所有操作模块列表")
    public ResponseEntity<Map<String, Object>> getAllModules() {
        List<String> modules = operationLogService.getAllModules();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", modules);
        return ResponseEntity.ok(response);
    }
}
