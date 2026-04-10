package com.student.management.repository;

import com.student.management.entity.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("SELECT g FROM Grade g WHERE g.studentId = :studentId")
    List<Grade> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT g FROM Grade g WHERE g.courseId = :courseId")
    List<Grade> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT g FROM Grade g WHERE g.studentId = :studentId AND g.courseId = :courseId")
    Optional<Grade> findByStudentIdAndCourseId(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.studentId = :studentId")
    Double getAverageScoreByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.courseId = :courseId")
    Double getAverageScoreByCourseId(@Param("courseId") Long courseId);

    Page<Grade> findAll(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Grade g WHERE g.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Modifying
    @Query("DELETE FROM Grade g WHERE g.courseId = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);
}
