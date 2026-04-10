package com.student.management.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeDTO {
    private Long id;
    
    @NotNull(message = "学生ID不能为空")
    private Long studentId;
    
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
    
    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0.0", message = "成绩不能小于0")
    @DecimalMax(value = "100.0", message = "成绩不能大于100")
    private BigDecimal score;
    
    private String studentName;
    private String courseName;
}
