package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.dto.ClazzDTO;
import com.student.management.entity.Clazz;
import com.student.management.service.IClazzService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班级管理控制器
 */
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "班级管理", description = "班级信息的增删改查接口")
public class ClazzController {

    private final IClazzService clazzService;

    @GetMapping
    @Operation(summary = "获取所有班级")
    public ResponseEntity<Result<List<Clazz>>> getAllClasses() {
        List<Clazz> classes = clazzService.getAllClasses();
        return Result.success("查询成功", classes).toResponseEntity();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取班级")
    public ResponseEntity<Result<Clazz>> getClassById(@PathVariable Long id) {
        Clazz clazz = clazzService.getClassById(id);
        return Result.success("查询成功", clazz).toResponseEntity();
    }

    @PostMapping
    @Operation(summary = "创建新班级")
    public ResponseEntity<Result<Clazz>> createClass(@Valid @RequestBody ClazzDTO clazzDTO) {
        Clazz created = clazzService.createClass(clazzDTO);
        return Result.created(created).toResponseEntity();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新班级信息")
    public ResponseEntity<Result<Clazz>> updateClass(
            @PathVariable Long id,
            @Valid @RequestBody ClazzDTO clazzDTO) {
        Clazz updated = clazzService.updateClass(id, clazzDTO);
        return Result.success("更新成功", updated).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除班级")
    public ResponseEntity<Result<Void>> deleteClass(@PathVariable Long id) {
        clazzService.deleteClass(id);
        return Result.<Void>success("删除成功").toResponseEntity();
    }
}
