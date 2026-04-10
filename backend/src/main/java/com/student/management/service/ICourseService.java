package com.student.management.service;

import com.student.management.dto.CourseDTO;
import com.student.management.entity.Course;

import java.util.List;

/**
 * 课程服务接口
 */
public interface ICourseService {

    /**
     * 获取所有课程
     */
    List<Course> getAllCourses();

    /**
     * 根据ID获取课程
     */
    Course getCourseById(Long id);

    /**
     * 创建课程
     */
    Course createCourse(CourseDTO courseDTO);

    /**
     * 更新课程
     */
    Course updateCourse(Long id, CourseDTO courseDTO);

    /**
     * 删除课程
     */
    void deleteCourse(Long id);

    /**
     * 搜索课程
     */
    List<Course> searchCourses(String name);

    /**
     * 获取课程总数
     */
    long getTotalCount();
}
