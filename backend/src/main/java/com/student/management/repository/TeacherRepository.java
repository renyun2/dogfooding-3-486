package com.student.management.repository;

import com.student.management.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByTeacherNo(String teacherNo);
    List<Teacher> findByNameContaining(String name);
    boolean existsByEmail(String email);
    boolean existsByTeacherNo(String teacherNo);
}
