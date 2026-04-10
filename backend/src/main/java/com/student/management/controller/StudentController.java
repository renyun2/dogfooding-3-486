package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.entity.Student;
import com.student.management.service.IStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(Result.success("查询成功", students));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取学生")
    public ResponseEntity<Result<Student>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(Result.success("查询成功", student));
    }

    @PostMapping
    @Operation(summary = "创建新学生")
    public ResponseEntity<Result<Student>> createStudent(@Valid @RequestBody Student student) {
        Student created = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.success("创建成功", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新学生信息")
    public ResponseEntity<Result<Student>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {
        Student updated = studentService.updateStudent(id, student);
        return ResponseEntity.ok(Result.success("更新成功", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    public ResponseEntity<Result<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(Result.success("删除成功"));
    }

    @GetMapping("/search")
    @Operation(summary = "按姓名搜索学生")
    public ResponseEntity<Result<List<Student>>> searchStudents(@RequestParam String name) {
        List<Student> students = studentService.searchStudents(name);
        return ResponseEntity.ok(Result.success("搜索成功", students));
    }

    @GetMapping("/count")
    @Operation(summary = "获取学生总数")
    public ResponseEntity<Result<Long>> getStudentCount() {
        long count = studentService.getTotalCount();
        return ResponseEntity.ok(Result.success("查询成功", count));
    }
}
