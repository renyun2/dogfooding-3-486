package com.student.management.repository;

import com.student.management.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByCourseId(Long courseId);

    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.studentId = ?1")
    Double getAverageScoreByStudentId(Long studentId);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.courseId = ?1")
    Double getAverageScoreByCourseId(Long courseId);
}
