package com.student.management.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseDTO {

    private Long id;

    @NotBlank(message = "课程名称不能为空")
    @Size(min = 2, max = 100, message = "课程名称长度在2到100个字符")
    private String name;

    @NotNull(message = "学分不能为空")
    @Min(value = 1, message = "学分至少为1")
    @Max(value = 10, message = "学分最多为10")
    private Integer credits;

    private String instructor;

    private String description;
}
