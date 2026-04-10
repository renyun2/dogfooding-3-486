package com.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程数据传输对象
 */
@Data
@Schema(description = "课程DTO")
public class CourseDTO {

    @Schema(description = "课程ID", example = "1")
    private Long id;

    @NotBlank(message = "课程名称不能为空")
    @Size(min = 2, max = 100, message = "课程名称长度必须在2-100之间")
    @Schema(description = "课程名称", example = "Java程序设计", required = true)
    private String name;

    @NotNull(message = "学分不能为空")
    @Min(value = 1, message = "学分必须大于0")
    @Max(value = 10, message = "学分不能超过10")
    @Schema(description = "学分", example = "4", required = true)
    private Integer credits;

    @NotBlank(message = "授课教师不能为空")
    @Size(min = 2, max = 50, message = "教师姓名长度必须在2-50之间")
    @Schema(description = "授课教师", example = "李老师", required = true)
    private String instructor;

    @Size(max = 500, message = "课程描述不能超过500字")
    @Schema(description = "课程描述", example = "面向对象的Java编程基础课程")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
