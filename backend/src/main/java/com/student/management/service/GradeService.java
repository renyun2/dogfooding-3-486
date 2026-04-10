package com.student.management.service;

import com.student.management.dto.GradeDTO;
import com.student.management.entity.Grade;
import com.student.management.exception.DuplicateResourceException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成绩服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GradeService implements IGradeService {

    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    @Override
    public List<Grade> getAllGrades() {
        log.info("Fetching all grades");
        return gradeRepository.findAll();
    }

    @Override
    public Grade getGradeById(Long id) {
        log.info("Fetching grade with id: {}", id);
        return gradeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Grade not found with id: {}", id);
                    return new ResourceNotFoundException("成绩记录", id);
                });
    }

    @Override
    public List<Grade> getGradesByStudentId(Long studentId) {
        log.info("Fetching grades for student id: {}", studentId);
        return gradeRepository.findByStudentId(studentId);
    }

    @Override
    public List<Grade> getGradesByCourseId(Long courseId) {
        log.info("Fetching grades for course id: {}", courseId);
        return gradeRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional
    public Grade createGrade(GradeDTO gradeDTO) {
        log.info("Creating new grade for student {} and course {}",
                gradeDTO.getStudentId(), gradeDTO.getCourseId());

        // Validate student and course exist
        studentService.getStudentById(gradeDTO.getStudentId());
        courseService.getCourseById(gradeDTO.getCourseId());

        // Check if grade already exists for this student-course combination
        if (gradeRepository.findByStudentIdAndCourseId(
                gradeDTO.getStudentId(), gradeDTO.getCourseId()).isPresent()) {
            log.error("Grade already exists for student {} and course {}",
                    gradeDTO.getStudentId(), gradeDTO.getCourseId());
            throw new DuplicateResourceException("成绩记录",
                    "studentId+courseId", gradeDTO.getStudentId() + "+" + gradeDTO.getCourseId());
        }

        Grade grade = convertToEntity(gradeDTO);
        Grade saved = gradeRepository.save(grade);
        log.info("Grade created successfully with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Grade updateGrade(Long id, GradeDTO gradeDTO) {
        log.info("Updating grade with id: {}", id);

        Grade existing = getGradeById(id);
        existing.setScore(gradeDTO.getScore());

        Grade updated = gradeRepository.save(existing);
        log.info("Grade updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Override
    @Transactional
    public void deleteGrade(Long id) {
        log.info("Deleting grade with id: {}", id);
        Grade grade = getGradeById(id);
        gradeRepository.delete(grade);
        log.info("Grade deleted successfully with id: {}", id);
    }

    @Override
    public Map<String, Object> getStatistics() {
        log.info("Fetching grade statistics");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalGrades", gradeRepository.count());

        return stats;
    }

    @Override
    public Double getAverageScoreByStudent(Long studentId) {
        log.info("Calculating average score for student: {}", studentId);
        Double avg = gradeRepository.getAverageScoreByStudentId(studentId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public Double getAverageScoreByCourse(Long courseId) {
        log.info("Calculating average score for course: {}", courseId);
        Double avg = gradeRepository.getAverageScoreByCourseId(courseId);
        return avg != null ? avg : 0.0;
    }

    /**
     * 将DTO转换为实体
     */
    private Grade convertToEntity(GradeDTO dto) {
        Grade grade = new Grade();
        grade.setStudentId(dto.getStudentId());
        grade.setCourseId(dto.getCourseId());
        grade.setScore(dto.getScore());
        return grade;
    }
}
