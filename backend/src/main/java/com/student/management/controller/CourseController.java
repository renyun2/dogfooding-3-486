package com.student.management.controller;

import com.student.management.annotation.LogOperation;
import com.student.management.entity.Course;
import com.student.management.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "课程管理", description = "课程信息的增删改查接口")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "获取所有课程")
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", courses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取课程")
    public ResponseEntity<Map<String, Object>> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", course);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "创建新课程")
    @LogOperation(module = "课程管理", type = "新增", description = "创建新课程")
    public ResponseEntity<Map<String, Object>> createCourse(@Valid @RequestBody Course course) {
        Course created = courseService.createCourse(course);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);
        response.put("message", "创建成功");
        response.put("data", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新课程信息")
    @LogOperation(module = "课程管理", type = "更新", description = "更新课程信息")
    public ResponseEntity<Map<String, Object>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody Course course) {
        Course updated = courseService.updateCourse(id, course);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程")
    @LogOperation(module = "课程管理", type = "删除", description = "删除课程")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "按名称搜索课程")
    public ResponseEntity<Map<String, Object>> searchCourses(@RequestParam String name) {
        List<Course> courses = courseService.searchCourses(name);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "搜索成功");
        response.put("data", courses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @Operation(summary = "获取课程总数")
    public ResponseEntity<Map<String, Object>> getCourseCount() {
        long count = courseService.getTotalCount();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }
}
