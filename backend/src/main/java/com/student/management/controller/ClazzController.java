package com.student.management.controller;

import com.student.management.annotation.OperationLog;
import com.student.management.entity.Clazz;
import com.student.management.service.ClazzService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "班级管理", description = "班级信息的增删改查接口")
public class ClazzController {

    private final ClazzService clazzService;

    @GetMapping
    @Operation(summary = "获取所有班级")
    @OperationLog(module = "班级管理", operationType = "查询", description = "查询所有班级列表")
    public ResponseEntity<Map<String, Object>> getAllClasses() {
        List<Clazz> classes = clazzService.getAllClasses();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", classes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取班级")
    @OperationLog(module = "班级管理", operationType = "查询", description = "根据ID查询班级详情")
    public ResponseEntity<Map<String, Object>> getClassById(@PathVariable Long id) {
        Clazz clazz = clazzService.getClassById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", clazz);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "创建新班级")
    @OperationLog(module = "班级管理", operationType = "新增", description = "创建新班级")
    public ResponseEntity<Map<String, Object>> createClass(@Valid @RequestBody Clazz clazz) {
        Clazz created = clazzService.createClass(clazz);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);
        response.put("message", "创建成功");
        response.put("data", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新班级信息")
    @OperationLog(module = "班级管理", operationType = "更新", description = "更新班级信息")
    public ResponseEntity<Map<String, Object>> updateClass(@PathVariable Long id, @Valid @RequestBody Clazz clazz) {
        Clazz updated = clazzService.updateClass(id, clazz);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除班级")
    @OperationLog(module = "班级管理", operationType = "删除", description = "删除班级")
    public ResponseEntity<Map<String, Object>> deleteClass(@PathVariable Long id) {
        clazzService.deleteClass(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}
