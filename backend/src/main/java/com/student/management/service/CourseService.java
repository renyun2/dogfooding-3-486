package com.student.management.service;

import com.student.management.entity.Course;
import com.student.management.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        log.info("Fetching all courses");
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        log.info("Fetching course with id: {}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found with id: {}", id);
                    return new RuntimeException("课程不存在，ID: " + id);
                });
    }

    @Transactional
    public Course createCourse(Course course) {
        log.info("Creating new course: {}", course.getName());

        if (courseRepository.existsByName(course.getName())) {
            log.error("Course name already exists: {}", course.getName());
            throw new RuntimeException("课程名称已存在: " + course.getName());
        }

        Course saved = courseRepository.save(course);
        log.info("Course created successfully with id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Course updateCourse(Long id, Course course) {
        log.info("Updating course with id: {}", id);

        Course existing = getCourseById(id);

        // Check name uniqueness if changed
        if (!existing.getName().equals(course.getName())
                && courseRepository.existsByName(course.getName())) {
            log.error("Course name already exists: {}", course.getName());
            throw new RuntimeException("课程名称已存在: " + course.getName());
        }

        existing.setName(course.getName());
        existing.setCredits(course.getCredits());
        existing.setTeacherId(course.getTeacherId());
        existing.setDescription(course.getDescription());

        Course updated = courseRepository.save(existing);
        log.info("Course updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Transactional
    public void deleteCourse(Long id) {
        log.info("Deleting course with id: {}", id);
        Course course = getCourseById(id);
        courseRepository.delete(course);
        log.info("Course deleted successfully with id: {}", id);
    }

    public List<Course> searchCourses(String name) {
        log.info("Searching courses with name containing: {}", name);
        return courseRepository.findByNameContaining(name);
    }

    public long getTotalCount() {
        log.info("Fetching total course count");
        return courseRepository.count();
    }
}
