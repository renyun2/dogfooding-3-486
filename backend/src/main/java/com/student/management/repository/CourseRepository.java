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

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.name = :name")
    Optional<Course> findByName(@Param("name") String name);

    @Query("SELECT c FROM Course c WHERE c.name LIKE %:name%")
    List<Course> findByNameContaining(@Param("name") String name);

    @Query("SELECT c FROM Course c WHERE c.name LIKE %:name%")
    Page<Course> findByNameContaining(@Param("name") String name, Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);

    Page<Course> findAll(Pageable pageable);
}
