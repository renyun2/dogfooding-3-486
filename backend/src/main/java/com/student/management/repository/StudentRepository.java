package com.student.management.repository;

import com.student.management.entity.Student;
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
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.email = :email")
    Optional<Student> findByEmail(@Param("email") String email);

    @Query("SELECT s FROM Student s WHERE s.name LIKE %:name%")
    List<Student> findByNameContaining(@Param("name") String name);

    @Query("SELECT s FROM Student s WHERE s.name LIKE %:name%")
    Page<Student> findByNameContaining(@Param("name") String name, Pageable pageable);

    @Query("SELECT COUNT(s) > 0 FROM Student s WHERE s.email = :email")
    boolean existsByEmail(@Param("email") String email);

    Page<Student> findAll(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Student s WHERE s.clazzId = :clazzId")
    void deleteByClazzId(@Param("clazzId") Long clazzId);
}
