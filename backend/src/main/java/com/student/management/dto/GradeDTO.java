package com.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成绩数据传输对象
 */
@Data
@Schema(description = "成绩DTO")
public class GradeDTO {

    @Schema(description = "成绩ID", example = "1")
    private Long id;

    @NotNull(message = "学生ID不能为空")
    @Schema(description = "学生ID", example = "1", required = true)
    private Long studentId;

    @Schema(description = "学生姓名", example = "张三")
    private String studentName;

    @NotNull(message = "课程ID不能为空")
    @Schema(description = "课程ID", example = "1", required = true)
    private Long courseId;

    @Schema(description = "课程名称", example = "Java程序设计")
    private String courseName;

    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0.0", message = "成绩不能小于0")
    @DecimalMax(value = "100.0", message = "成绩不能大于100")
    @Schema(description = "成绩", example = "85.5", required = true)
    private BigDecimal score;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
