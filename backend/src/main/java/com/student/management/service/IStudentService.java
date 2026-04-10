package com.student.management.service;

import com.student.management.dto.StudentDTO;
import com.student.management.entity.Student;

import java.util.List;

/**
 * 学生服务接口
 */
public interface IStudentService {

    /**
     * 获取所有学生
     */
    List<Student> getAllStudents();

    /**
     * 根据ID获取学生
     */
    Student getStudentById(Long id);

    /**
     * 创建学生
     */
    Student createStudent(StudentDTO studentDTO);

    /**
     * 更新学生
     */
    Student updateStudent(Long id, StudentDTO studentDTO);

    /**
     * 删除学生
     */
    void deleteStudent(Long id);

    /**
     * 搜索学生
     */
    List<Student> searchStudents(String name);

    /**
     * 获取学生总数
     */
    long getTotalCount();
}
