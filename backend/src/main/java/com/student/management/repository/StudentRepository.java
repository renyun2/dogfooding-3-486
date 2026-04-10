package com.student.management.repository;

import com.student.management.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * 根据邮箱查询学生
     */
    Optional<Student> findByEmail(String email);

    /**
     * 根据姓名模糊查询
     */
    List<Student> findByNameContaining(String name);

    /**
     * 根据姓名模糊查询（分页）
     */
    Page<Student> findByNameContaining(String name, Pageable pageable);

    /**
     * 根据班级ID查询学生
     */
    List<Student> findByClazzId(Long clazzId);

    /**
     * 根据班级ID查询学生（分页）
     */
    Page<Student> findByClazzId(Long clazzId, Pageable pageable);

    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查班级下是否有学生
     */
    boolean existsByClazzId(Long clazzId);

    /**
     * 自定义查询：根据多个条件搜索学生
     */
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR s.name LIKE %:name%) AND " +
           "(:gender IS NULL OR s.gender = :gender) AND " +
           "(:clazzId IS NULL OR s.clazzId = :clazzId)")
    Page<Student> searchStudents(
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("clazzId") Long clazzId,
            Pageable pageable);

    /**
     * 统计各班级学生数量
     */
    @Query("SELECT s.clazzId, COUNT(s) FROM Student s GROUP BY s.clazzId")
    List<Object[]> countStudentsByClazz();
}
