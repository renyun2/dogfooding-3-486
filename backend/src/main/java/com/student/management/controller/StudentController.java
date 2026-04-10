package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.dto.StudentDTO;
import com.student.management.entity.Student;
import com.student.management.service.IStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生管理控制器
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "学生管理", description = "学生信息的增删改查接口")
public class StudentController {

    private final IStudentService studentService;

    @GetMapping
    @Operation(summary = "获取所有学生")
    public ResponseEntity<Result<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return Result.success("查询成功", students).toResponseEntity();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取学生")
    public ResponseEntity<Result<Student>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return Result.success("查询成功", student).toResponseEntity();
    }

    @PostMapping
    @Operation(summary = "创建新学生")
    public ResponseEntity<Result<Student>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        Student created = studentService.createStudent(studentDTO);
        return Result.created(created).toResponseEntity();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新学生信息")
    public ResponseEntity<Result<Student>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO) {
        Student updated = studentService.updateStudent(id, studentDTO);
        return Result.success("更新成功", updated).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    public ResponseEntity<Result<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return Result.<Void>success("删除成功").toResponseEntity();
    }

    @GetMapping("/search")
    @Operation(summary = "按姓名搜索学生")
    public ResponseEntity<Result<List<Student>>> searchStudents(@RequestParam String name) {
        List<Student> students = studentService.searchStudents(name);
        return Result.success("搜索成功", students).toResponseEntity();
    }

    @GetMapping("/count")
    @Operation(summary = "获取学生总数")
    public ResponseEntity<Result<Long>> getStudentCount() {
        long count = studentService.getTotalCount();
        return Result.success("查询成功", count).toResponseEntity();
    }
}
