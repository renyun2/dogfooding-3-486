package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.entity.Grade;
import com.student.management.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Result<List<Grade>>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(Result.success("查询成功", grades));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取成绩")
    public ResponseEntity<Result<Grade>> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(Result.success("查询成功", grade));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "获取某学生的所有成绩")
    public ResponseEntity<Result<List<Grade>>> getGradesByStudentId(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudentId(studentId);
        return ResponseEntity.ok(Result.success("查询成功", grades));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "获取某课程的所有成绩")
    public ResponseEntity<Result<List<Grade>>> getGradesByCourseId(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByCourseId(courseId);
        return ResponseEntity.ok(Result.success("查询成功", grades));
    }

    @PostMapping
    @Operation(summary = "创建新成绩")
    public ResponseEntity<Result<Grade>> createGrade(@Valid @RequestBody Grade grade) {
        Grade created = gradeService.createGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.success(201, "创建成功", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新成绩")
    public ResponseEntity<Result<Grade>> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody Grade grade) {
        Grade updated = gradeService.updateGrade(id, grade);
        return ResponseEntity.ok(Result.success("更新成功", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除成绩")
    public ResponseEntity<Result<Void>> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(Result.success("删除成功", null));
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取成绩统计")
    public ResponseEntity<Result<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = gradeService.getStatistics();
        return ResponseEntity.ok(Result.success("查询成功", stats));
    }

    @GetMapping("/average/student/{studentId}")
    @Operation(summary = "获取学生平均分")
    public ResponseEntity<Result<Double>> getStudentAverageScore(@PathVariable Long studentId) {
        Double average = gradeService.getAverageScoreByStudent(studentId);
        return ResponseEntity.ok(Result.success("查询成功", average));
    }

    @GetMapping("/average/course/{courseId}")
    @Operation(summary = "获取课程平均分")
    public ResponseEntity<Result<Double>> getCourseAverageScore(@PathVariable Long courseId) {
        Double average = gradeService.getAverageScoreByCourse(courseId);
        return ResponseEntity.ok(Result.success("查询成功", average));
    }
}
