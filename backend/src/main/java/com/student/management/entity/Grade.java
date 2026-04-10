package com.student.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "grades", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "student_id", "course_id" })
})
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "student", "course" })
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "学生ID不能为空")
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @NotNull(message = "课程ID不能为空")
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0.0", message = "成绩不能小于0")
    @DecimalMax(value = "100.0", message = "成绩不能大于100")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Course course;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
