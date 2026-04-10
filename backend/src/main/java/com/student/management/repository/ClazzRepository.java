package com.student.management.repository;

import com.student.management.entity.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz, Long> {
    Optional<Clazz> findByName(String name);

    boolean existsByName(String name);
}
