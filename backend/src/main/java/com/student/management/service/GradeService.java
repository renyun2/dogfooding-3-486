package com.student.management.service;

import com.student.management.entity.Grade;
import com.student.management.repository.GradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GradeService {

    private static final Logger log = LoggerFactory.getLogger(GradeService.class);

    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    public GradeService(GradeRepository gradeRepository, StudentService studentService, CourseService courseService) {
        this.gradeRepository = gradeRepository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public List<Grade> getAllGrades() {
        log.info("Fetching all grades");
        return gradeRepository.findAll();
    }

    public Grade getGradeById(Long id) {
        log.info("Fetching grade with id: {}", id);
        return gradeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Grade not found with id: {}", id);
                    return new RuntimeException("成绩记录不存在，ID: " + id);
                });
    }

    public List<Grade> getGradesByStudentId(Long studentId) {
        log.info("Fetching grades for student id: {}", studentId);
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByCourseId(Long courseId) {
        log.info("Fetching grades for course id: {}", courseId);
        return gradeRepository.findByCourseId(courseId);
    }

    @Transactional
    public Grade createGrade(Grade grade) {
        log.info("Creating new grade for student {} and course {}",
                grade.getStudentId(), grade.getCourseId());

        // Validate student and course exist
        studentService.getStudentById(grade.getStudentId());
        courseService.getCourseById(grade.getCourseId());

        // Check if grade already exists for this student-course combination
        if (gradeRepository.findByStudentIdAndCourseId(
                grade.getStudentId(), grade.getCourseId()).isPresent()) {
            log.error("Grade already exists for student {} and course {}",
                    grade.getStudentId(), grade.getCourseId());
            throw new RuntimeException("该学生在此课程中已有成绩记录");
        }

        Grade saved = gradeRepository.save(grade);
        log.info("Grade created successfully with id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Grade updateGrade(Long id, Grade grade) {
        log.info("Updating grade with id: {}", id);

        Grade existing = getGradeById(id);
        existing.setScore(grade.getScore());

        Grade updated = gradeRepository.save(existing);
        log.info("Grade updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Transactional
    public void deleteGrade(Long id) {
        log.info("Deleting grade with id: {}", id);
        Grade grade = getGradeById(id);
        gradeRepository.delete(grade);
        log.info("Grade deleted successfully with id: {}", id);
    }

    public Map<String, Object> getStatistics() {
        log.info("Fetching grade statistics");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalGrades", gradeRepository.count());

        return stats;
    }

    public Double getAverageScoreByStudent(Long studentId) {
        log.info("Calculating average score for student: {}", studentId);
        Double avg = gradeRepository.getAverageScoreByStudentId(studentId);
        return avg != null ? avg : 0.0;
    }

    public Double getAverageScoreByCourse(Long courseId) {
        log.info("Calculating average score for course: {}", courseId);
        Double avg = gradeRepository.getAverageScoreByCourseId(courseId);
        return avg != null ? avg : 0.0;
    }
}
