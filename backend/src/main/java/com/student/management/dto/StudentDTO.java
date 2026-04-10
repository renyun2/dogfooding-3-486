package com.student.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * 学生数据传输对象
 */
@Data
@Schema(description = "学生DTO")
public class StudentDTO {

    @Schema(description = "学生ID", example = "1")
    private Long id;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度必须在2-50之间")
    @Schema(description = "姓名", example = "张三", required = true)
    private String name;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别必须为男或女")
    @Schema(description = "性别", example = "男", allowableValues = {"男", "女"}, required = true)
    private String gender;

    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    @Schema(description = "年龄", example = "20", required = true)
    private Integer age;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "zhangsan@example.com", required = true)
    private String email;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000", required = true)
    private String phone;

    @NotNull(message = "入学日期不能为空")
    @Schema(description = "入学日期", example = "2024-09-01", required = true)
    private LocalDate enrollmentDate;

    @Schema(description = "班级ID", example = "1")
    private Long clazzId;

    @Schema(description = "班级名称", example = "计算机1班")
    private String clazzName;
}
