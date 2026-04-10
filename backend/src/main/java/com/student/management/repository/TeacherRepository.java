package com.student.management.repository;

import com.student.management.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByEmployeeId(String employeeId);
    
    List<Teacher> findByNameContaining(String name);
    
    List<Teacher> findByDepartment(String department);
    
    boolean existsByEmployeeId(String employeeId);
    
    long countByDepartment(String department);
}
