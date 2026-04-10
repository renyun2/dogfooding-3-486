package com.student.management.repository;

import com.student.management.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 课程数据访问层
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * 根据名称查询课程
     */
    Optional<Course> findByName(String name);

    /**
     * 根据名称模糊查询
     */
    List<Course> findByNameContaining(String name);

    /**
     * 根据名称模糊查询（分页）
     */
    Page<Course> findByNameContaining(String name, Pageable pageable);

    /**
     * 根据教师查询课程
     */
    List<Course> findByInstructor(String instructor);

    /**
     * 根据教师查询课程（分页）
     */
    Page<Course> findByInstructor(String instructor, Pageable pageable);

    /**
     * 检查课程名称是否已存在
     */
    boolean existsByName(String name);

    /**
     * 自定义查询：根据多个条件搜索课程
     */
    @Query("SELECT c FROM Course c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:instructor IS NULL OR c.instructor LIKE %:instructor%) AND " +
           "(:minCredits IS NULL OR c.credits >= :minCredits)")
    Page<Course> searchCourses(
            @Param("name") String name,
            @Param("instructor") String instructor,
            @Param("minCredits") Integer minCredits,
            Pageable pageable);

    /**
     * 统计课程总数
     */
    @Query("SELECT COUNT(c) FROM Course c")
    long countAll();
}
