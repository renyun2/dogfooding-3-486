package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.dto.GradeDTO;
import com.student.management.entity.Grade;
import com.student.management.service.IGradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 成绩管理控制器
 */
@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "成绩管理", description = "学生成绩的增删改查接口")
public class GradeController {

    private final IGradeService gradeService;

    @GetMapping
    @Operation(summary = "获取所有成绩")
    public ResponseEntity<Result<List<Grade>>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return Result.success("查询成功", grades).toResponseEntity();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取成绩")
    public ResponseEntity<Result<Grade>> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.getGradeById(id);
        return Result.success("查询成功", grade).toResponseEntity();
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "获取某学生的所有成绩")
    public ResponseEntity<Result<List<Grade>>> getGradesByStudentId(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudentId(studentId);
        return Result.success("查询成功", grades).toResponseEntity();
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "获取某课程的所有成绩")
    public ResponseEntity<Result<List<Grade>>> getGradesByCourseId(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByCourseId(courseId);
        return Result.success("查询成功", grades).toResponseEntity();
    }

    @PostMapping
    @Operation(summary = "创建新成绩")
    public ResponseEntity<Result<Grade>> createGrade(@Valid @RequestBody GradeDTO gradeDTO) {
        Grade created = gradeService.createGrade(gradeDTO);
        return Result.created(created).toResponseEntity();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新成绩")
    public ResponseEntity<Result<Grade>> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody GradeDTO gradeDTO) {
        Grade updated = gradeService.updateGrade(id, gradeDTO);
        return Result.success("更新成功", updated).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除成绩")
    public ResponseEntity<Result<Void>> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return Result.<Void>success("删除成功").toResponseEntity();
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取成绩统计")
    public ResponseEntity<Result<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = gradeService.getStatistics();
        return Result.success("查询成功", stats).toResponseEntity();
    }

    @GetMapping("/average/student/{studentId}")
    @Operation(summary = "获取学生平均分")
    public ResponseEntity<Result<Double>> getStudentAverageScore(@PathVariable Long studentId) {
        Double average = gradeService.getAverageScoreByStudent(studentId);
        return Result.success("查询成功", average).toResponseEntity();
    }

    @GetMapping("/average/course/{courseId}")
    @Operation(summary = "获取课程平均分")
    public ResponseEntity<Result<Double>> getCourseAverageScore(@PathVariable Long courseId) {
        Double average = gradeService.getAverageScoreByCourse(courseId);
        return Result.success("查询成功", average).toResponseEntity();
    }
}
