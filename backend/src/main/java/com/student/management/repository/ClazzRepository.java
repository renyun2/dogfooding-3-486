package com.student.management.repository;

import com.student.management.entity.Clazz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz, Long> {

    @Query("SELECT c FROM Clazz c WHERE c.name = :name")
    Optional<Clazz> findByName(@Param("name") String name);

    @Query("SELECT COUNT(c) > 0 FROM Clazz c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);

    Page<Clazz> findAll(Pageable pageable);
}
