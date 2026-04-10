package com.student.management.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GradeDTO {

    private Long id;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    private String studentName;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    private String courseName;

    @NotNull(message = "分数不能为空")
    @Min(value = 0, message = "分数不能小于0")
    @Max(value = 100, message = "分数不能大于100")
    private Integer score;
}
