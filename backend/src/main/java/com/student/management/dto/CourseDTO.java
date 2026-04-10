package com.student.management.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDTO {
    private Long id;
    
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 100, message = "课程名称不能超过100个字符")
    private String name;
    
    @NotNull(message = "学分不能为空")
    @DecimalMin(value = "0.5", message = "学分最小为0.5")
    @DecimalMax(value = "10.0", message = "学分最大为10.0")
    private BigDecimal credits;
    
    @Size(max = 50, message = "授课教师名称不能超过50个字符")
    private String instructor;
    
    @Size(max = 500, message = "描述不能超过500个字符")
    private String description;
}
