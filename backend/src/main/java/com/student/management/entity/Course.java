package com.student.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "courses")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "grades" })
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "课程名称不能为空")
    @Size(min = 2, max = 100, message = "课程名称长度必须在2-100之间")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @NotNull(message = "学分不能为空")
    @Min(value = 1, message = "学分必须大于0")
    @Max(value = 10, message = "学分不能超过10")
    @Column(nullable = false)
    private Integer credits;

    @NotBlank(message = "授课教师不能为空")
    @Size(min = 2, max = 50, message = "教师姓名长度必须在2-50之间")
    @Column(nullable = false, length = 50)
    private String instructor;

    @Size(max = 500, message = "课程描述不能超过500字")
    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private java.util.List<Grade> grades;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
