package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.entity.Course;
import com.student.management.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "课程管理", description = "课程信息的增删改查接口")
public class CourseController {

    private final ICourseService courseService;

    @GetMapping
    @Operation(summary = "获取所有课程")
    public ResponseEntity<Result<List<Course>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(Result.success("查询成功", courses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取课程")
    public ResponseEntity<Result<Course>> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(Result.success("查询成功", course));
    }

    @PostMapping
    @Operation(summary = "创建新课程")
    public ResponseEntity<Result<Course>> createCourse(@Valid @RequestBody Course course) {
        Course created = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.success("创建成功", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新课程信息")
    public ResponseEntity<Result<Course>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody Course course) {
        Course updated = courseService.updateCourse(id, course);
        return ResponseEntity.ok(Result.success("更新成功", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程")
    public ResponseEntity<Result<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(Result.success("删除成功"));
    }

    @GetMapping("/search")
    @Operation(summary = "按名称搜索课程")
    public ResponseEntity<Result<List<Course>>> searchCourses(@RequestParam String name) {
        List<Course> courses = courseService.searchCourses(name);
        return ResponseEntity.ok(Result.success("搜索成功", courses));
    }

    @GetMapping("/count")
    @Operation(summary = "获取课程总数")
    public ResponseEntity<Result<Long>> getCourseCount() {
        long count = courseService.getTotalCount();
        return ResponseEntity.ok(Result.success("查询成功", count));
    }
}
