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
    @Size(min = 3, max = 20, message = "工号长度必须在3-20之间")
    @Column(nullable = false, unique = true, length = 20)
    private String employeeId;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度必须在2-50之间")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别必须为'男'或'女'")
    @Column(nullable = false, length = 10)
    private String gender;

    @Size(max = 50, message = "职称长度不能超过50")
    @Column(length = 50)
    private String title;

    @Size(max = 100, message = "所属院系长度不能超过100")
    @Column(length = 100)
    private String department;

    @Pattern(regexp = "^(|1[3-9]\\d{9})$", message = "请输入有效的手机号码")
    @Column(length = 20)
    private String phone;

    @Email(message = "请输入有效的邮箱地址")
    @Size(max = 100, message = "邮箱长度不能超过100")
    @Column(length = 100)
    private String email;

    @NotNull(message = "入职日期不能为空")
    @PastOrPresent(message = "入职日期不能是未来日期")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> courses;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
