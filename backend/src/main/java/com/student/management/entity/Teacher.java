package com.student.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "teachers")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "courses" })
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "工号不能为空")
    @Size(min = 4, max = 20, message = "工号长度必须在4-20之间")
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
    @Size(min = 2, max = 30, message = "职称长度必须在2-30之间")
    @Column(nullable = false, length = 30)
    private String title;

    @NotBlank(message = "所属院系不能为空")
    @Size(min = 2, max = 100, message = "所属院系长度必须在2-100之间")
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

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.SET_NULL)
    private List<Course> courses;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
