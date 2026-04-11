package com.student.management.controller;

import com.student.management.annotation.OperationLog;
import com.student.management.entity.Student;
import com.student.management.service.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "学生管理", description = "学生信息的增删改查接口")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "获取所有学生")
    @OperationLog(module = "学生管理", operationType = "查询", description = "查询所有学生列表")
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", students);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取学生")
    @OperationLog(module = "学生管理", operationType = "查询", description = "根据ID查询学生详情")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", student);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "创建新学生")
    @OperationLog(module = "学生管理", operationType = "新增", description = "创建新学生")
    public ResponseEntity<Map<String, Object>> createStudent(@Valid @RequestBody Student student) {
        Student created = studentService.createStudent(student);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);
        response.put("message", "创建成功");
        response.put("data", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新学生信息")
    @OperationLog(module = "学生管理", operationType = "更新", description = "更新学生信息")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {
        Student updated = studentService.updateStudent(id, student);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    @OperationLog(module = "学生管理", operationType = "删除", description = "删除学生")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "按姓名搜索学生")
    @OperationLog(module = "学生管理", operationType = "查询", description = "按姓名搜索学生")
    public ResponseEntity<Map<String, Object>> searchStudents(@RequestParam String name) {
        List<Student> students = studentService.searchStudents(name);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "搜索成功");
        response.put("data", students);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @Operation(summary = "获取学生总数")
    public ResponseEntity<Map<String, Object>> getStudentCount() {
        long count = studentService.getTotalCount();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }
}
