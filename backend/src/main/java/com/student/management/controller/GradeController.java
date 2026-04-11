package com.student.management.controller;

import com.student.management.annotation.OperationLog;
import com.student.management.entity.Grade;
import com.student.management.service.GradeService;
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
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "成绩管理", description = "学生成绩的增删改查接口")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    @Operation(summary = "获取所有成绩")
    public ResponseEntity<Map<String, Object>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", grades);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取成绩")
    public ResponseEntity<Map<String, Object>> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.getGradeById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", grade);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "获取某学生的所有成绩")
    public ResponseEntity<Map<String, Object>> getGradesByStudentId(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudentId(studentId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", grades);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "获取某课程的所有成绩")
    public ResponseEntity<Map<String, Object>> getGradesByCourseId(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByCourseId(courseId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", grades);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "创建新成绩")
    @OperationLog(module = "成绩管理", type = "新增")
    public ResponseEntity<Map<String, Object>> createGrade(@Valid @RequestBody Grade grade) {
        Grade created = gradeService.createGrade(grade);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);
        response.put("message", "创建成功");
        response.put("data", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新成绩")
    @OperationLog(module = "成绩管理", type = "修改")
    public ResponseEntity<Map<String, Object>> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody Grade grade) {
        Grade updated = gradeService.updateGrade(id, grade);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除成绩")
    @OperationLog(module = "成绩管理", type = "删除")
    public ResponseEntity<Map<String, Object>> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取成绩统计")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = gradeService.getStatistics();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", stats);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/average/student/{studentId}")
    @Operation(summary = "获取学生平均分")
    public ResponseEntity<Map<String, Object>> getStudentAverageScore(@PathVariable Long studentId) {
        Double average = gradeService.getAverageScoreByStudent(studentId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", average);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/average/course/{courseId}")
    @Operation(summary = "获取课程平均分")
    public ResponseEntity<Map<String, Object>> getCourseAverageScore(@PathVariable Long courseId) {
        Double average = gradeService.getAverageScoreByCourse(courseId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", average);
        return ResponseEntity.ok(response);
    }
}
