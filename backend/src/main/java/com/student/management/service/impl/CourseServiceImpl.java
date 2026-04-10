package com.student.management.service.impl;

import com.student.management.entity.Course;
import com.student.management.exception.BusinessException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.CourseRepository;
import com.student.management.service.ICourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        log.info("Fetching all courses");
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        log.info("Fetching course with id: {}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在，ID: " + id));
    }

    @Override
    @Transactional
    public Course createCourse(Course course) {
        log.info("Creating new course: {}", course.getName());

        if (courseRepository.existsByName(course.getName())) {
            throw new BusinessException("课程名称已存在: " + course.getName());
        }

        Course saved = courseRepository.save(course);
        log.info("Course created successfully with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Course updateCourse(Long id, Course course) {
        log.info("Updating course with id: {}", id);

        Course existing = getCourseById(id);

        if (!existing.getName().equals(course.getName())
                && courseRepository.existsByName(course.getName())) {
            throw new BusinessException("课程名称已存在: " + course.getName());
        }

        existing.setName(course.getName());
        existing.setCredits(course.getCredits());
        existing.setInstructor(course.getInstructor());
        existing.setDescription(course.getDescription());

        Course updated = courseRepository.save(existing);
        log.info("Course updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        log.info("Deleting course with id: {}", id);
        Course course = getCourseById(id);
        courseRepository.delete(course);
        log.info("Course deleted successfully with id: {}", id);
    }

    @Override
    public List<Course> searchCourses(String name) {
        log.info("Searching courses with name containing: {}", name);
        return courseRepository.findByNameContaining(name);
    }

    @Override
    public long getTotalCount() {
        log.info("Fetching total course count");
        return courseRepository.count();
    }
}
