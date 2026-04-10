package com.student.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClazzDTO {
    private Long id;
    
    @NotBlank(message = "班级名称不能为空")
    @Size(max = 100, message = "班级名称不能超过100个字符")
    private String name;
    
    @Size(max = 500, message = "描述不能超过500个字符")
    private String description;
}
