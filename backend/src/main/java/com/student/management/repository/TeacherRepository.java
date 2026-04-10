package com.student.management.repository;

import com.student.management.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByTeacherNo(String teacherNo);

    Optional<Teacher> findByEmail(String email);

    List<Teacher> findByNameContaining(String name);

    List<Teacher> findByDepartmentContaining(String department);

    boolean existsByTeacherNo(String teacherNo);

    boolean existsByEmail(String email);
}
