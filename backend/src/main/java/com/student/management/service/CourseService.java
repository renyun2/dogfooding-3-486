package com.student.management.service;

import com.student.management.dto.CourseDTO;
import com.student.management.entity.Course;
import com.student.management.exception.DuplicateResourceException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

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
                .orElseThrow(() -> {
                    log.error("Course not found with id: {}", id);
                    return new ResourceNotFoundException("课程", id);
                });
    }

    @Override
    @Transactional
    public Course createCourse(CourseDTO courseDTO) {
        log.info("Creating new course: {}", courseDTO.getName());

        if (courseRepository.existsByName(courseDTO.getName())) {
            log.error("Course name already exists: {}", courseDTO.getName());
            throw new DuplicateResourceException("课程名称", "name", courseDTO.getName());
        }

        Course course = convertToEntity(courseDTO);
        Course saved = courseRepository.save(course);
        log.info("Course created successfully with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        log.info("Updating course with id: {}", id);

        Course existing = getCourseById(id);

        // Check name uniqueness if changed
        if (!existing.getName().equals(courseDTO.getName())
                && courseRepository.existsByName(courseDTO.getName())) {
            log.error("Course name already exists: {}", courseDTO.getName());
            throw new DuplicateResourceException("课程名称", "name", courseDTO.getName());
        }

        updateEntityFromDTO(existing, courseDTO);

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

    /**
     * 将DTO转换为实体
     */
    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setCredits(dto.getCredits());
        course.setInstructor(dto.getInstructor());
        course.setDescription(dto.getDescription());
        return course;
    }

    /**
     * 使用DTO更新实体
     */
    private void updateEntityFromDTO(Course course, CourseDTO dto) {
        course.setName(dto.getName());
        course.setCredits(dto.getCredits());
        course.setInstructor(dto.getInstructor());
        course.setDescription(dto.getDescription());
    }
}
