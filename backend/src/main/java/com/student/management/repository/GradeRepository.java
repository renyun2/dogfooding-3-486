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

/**
 * 成绩数据访问层
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * 根据学生ID查询成绩
     */
    List<Grade> findByStudentId(Long studentId);

    /**
     * 根据学生ID查询成绩（分页）
     */
    Page<Grade> findByStudentId(Long studentId, Pageable pageable);

    /**
     * 根据课程ID查询成绩
     */
    List<Grade> findByCourseId(Long courseId);

    /**
     * 根据课程ID查询成绩（分页）
     */
    Page<Grade> findByCourseId(Long courseId, Pageable pageable);

    /**
     * 根据学生ID和课程ID查询成绩
     */
    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 检查学生是否已有某课程的成绩
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 根据分数范围查询
     */
    List<Grade> findByScoreBetween(Integer minScore, Integer maxScore);

    /**
     * 根据分数范围查询（分页）
     */
    Page<Grade> findByScoreBetween(Integer minScore, Integer maxScore, Pageable pageable);

    /**
     * 计算学生平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.studentId = :studentId")
    Double getAverageScoreByStudentId(@Param("studentId") Long studentId);

    /**
     * 计算课程平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.courseId = :courseId")
    Double getAverageScoreByCourseId(@Param("courseId") Long courseId);

    /**
     * 计算学生总分
     */
    @Query("SELECT SUM(g.score) FROM Grade g WHERE g.studentId = :studentId")
    Integer getTotalScoreByStudentId(@Param("studentId") Long studentId);

    /**
     * 统计学生课程数量
     */
    @Query("SELECT COUNT(g) FROM Grade g WHERE g.studentId = :studentId")
    Long countByStudentId(@Param("studentId") Long studentId);

    /**
     * 统计课程学生数量
     */
    @Query("SELECT COUNT(g) FROM Grade g WHERE g.courseId = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);

    /**
     * 批量删除学生的所有成绩
     */
    @Modifying
    @Query("DELETE FROM Grade g WHERE g.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    /**
     * 批量删除课程的所有成绩
     */
    @Modifying
    @Query("DELETE FROM Grade g WHERE g.courseId = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);

    /**
     * 自定义查询：根据多个条件搜索成绩
     */
    @Query("SELECT g FROM Grade g WHERE " +
           "(:studentId IS NULL OR g.studentId = :studentId) AND " +
           "(:courseId IS NULL OR g.courseId = :courseId) AND " +
           "(:minScore IS NULL OR g.score >= :minScore) AND " +
           "(:maxScore IS NULL OR g.score <= :maxScore)")
    Page<Grade> searchGrades(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId,
            @Param("minScore") Integer minScore,
            @Param("maxScore") Integer maxScore,
            Pageable pageable);

    /**
     * 获取成绩分布统计
     */
    @Query("SELECT CASE " +
           "WHEN g.score >= 90 THEN '优秀' " +
           "WHEN g.score >= 80 THEN '良好' " +
           "WHEN g.score >= 70 THEN '中等' " +
           "WHEN g.score >= 60 THEN '及格' " +
           "ELSE '不及格' END as gradeLevel, " +
           "COUNT(g) as count " +
           "FROM Grade g GROUP BY gradeLevel")
    List<Object[]> getGradeDistribution();
}
