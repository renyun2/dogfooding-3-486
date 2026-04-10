package com.student.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "classes")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "students" })
public class Clazz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "班级名称不能为空")
    @Size(min = 2, max = 50, message = "班级名称长度必须在2-50之间")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Size(max = 200, message = "班级描述不能超过200字")
    @Column(length = 200)
    private String description;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Student> students;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
