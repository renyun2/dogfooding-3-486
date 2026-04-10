package com.student.management.service;

import com.student.management.entity.Course;

import java.util.List;

public interface ICourseService {
    
    List<Course> getAllCourses();
    
    Course getCourseById(Long id);
    
    Course createCourse(Course course);
    
    Course updateCourse(Long id, Course course);
    
    void deleteCourse(Long id);
    
    List<Course> searchCourses(String name);
    
    long getTotalCount();
}
