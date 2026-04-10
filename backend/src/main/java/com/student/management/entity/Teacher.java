package com.student.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "teachers")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "courses" })
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "工号不能为空")
    @Pattern(regexp = "^T\\d{5}$", message = "工号格式必须为T+5位数字，如T00001")
    @Column(nullable = false, unique = true, length = 20)
    private String teacherNo;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度必须在2-50之间")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别必须为男或女")
    @Column(nullable = false, length = 10)
    private String gender;

    @NotBlank(message = "职称不能为空")
    @Pattern(regexp = "^(教授|副教授|讲师|助教)$", message = "职称必须为教授、副教授、讲师或助教")
    @Column(nullable = false, length = 20)
    private String title;

    @NotBlank(message = "所属院系不能为空")
    @Size(min = 2, max = 100, message = "院系名称长度必须在2-100之间")
    @Column(nullable = false, length = 100)
    private String department;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotNull(message = "入职日期不能为空")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
