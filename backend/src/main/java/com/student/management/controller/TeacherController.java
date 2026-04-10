package com.student.management.controller;

import com.student.management.entity.Teacher;
import com.student.management.service.TeacherService;
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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "教师管理", description = "教师信息的增删改查接口")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "获取所有教师")
    public ResponseEntity<Map<String, Object>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", teachers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取教师")
    public ResponseEntity<Map<String, Object>> getTeacherById(@PathVariable Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", teacher);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "创建新教师")
    public ResponseEntity<Map<String, Object>> createTeacher(@Valid @RequestBody Teacher teacher) {
        Teacher created = teacherService.createTeacher(teacher);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);
        response.put("message", "创建成功");
        response.put("data", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新教师信息")
    public ResponseEntity<Map<String, Object>> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody Teacher teacher) {
        Teacher updated = teacherService.updateTeacher(id, teacher);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除教师")
    public ResponseEntity<Map<String, Object>> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "按姓名搜索教师")
    public ResponseEntity<Map<String, Object>> searchTeachers(@RequestParam String name) {
        List<Teacher> teachers = teacherService.searchTeachers(name);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "搜索成功");
        response.put("data", teachers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "按院系获取教师")
    public ResponseEntity<Map<String, Object>> getTeachersByDepartment(@PathVariable String department) {
        List<Teacher> teachers = teacherService.getTeachersByDepartment(department);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", teachers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @Operation(summary = "获取教师总数")
    public ResponseEntity<Map<String, Object>> getTeacherCount() {
        long count = teacherService.getTotalCount();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }
}
