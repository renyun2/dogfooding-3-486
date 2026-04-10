package com.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班级数据传输对象
 */
@Data
@Schema(description = "班级DTO")
public class ClazzDTO {

    @Schema(description = "班级ID", example = "1")
    private Long id;

    @NotBlank(message = "班级名称不能为空")
    @Size(min = 2, max = 50, message = "班级名称长度必须在2-50之间")
    @Schema(description = "班级名称", example = "计算机1班", required = true)
    private String name;

    @Size(max = 200, message = "班级描述不能超过200字")
    @Schema(description = "班级描述", example = "2024级计算机科学与技术专业1班")
    private String description;

    @Schema(description = "学生数量", example = "30")
    private Integer studentCount;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
