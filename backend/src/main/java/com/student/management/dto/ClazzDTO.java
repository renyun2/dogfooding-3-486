package com.student.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClazzDTO {

    private Long id;

    @NotBlank(message = "班级名称不能为空")
    @Size(min = 2, max = 100, message = "班级名称长度在2到100个字符")
    private String name;

    private String description;

    private Integer studentCount;
}
