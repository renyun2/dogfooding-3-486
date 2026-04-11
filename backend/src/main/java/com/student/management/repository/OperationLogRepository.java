package com.student.management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<com.student.management.entity.OperationLog, Long> {

    Page<com.student.management.entity.OperationLog> findByOperatorContaining(String operator, Pageable pageable);

    Page<com.student.management.entity.OperationLog> findByModule(String module, Pageable pageable);

    @Query("SELECT o FROM com.student.management.entity.OperationLog o WHERE " +
           "(:operator IS NULL OR o.operator LIKE %:operator%) AND " +
           "(:module IS NULL OR o.module = :module) AND " +
           "(:startTime IS NULL OR o.operationTime >= :startTime) AND " +
           "(:endTime IS NULL OR o.operationTime <= :endTime) AND " +
           "(:isSuccess IS NULL OR o.isSuccess = :isSuccess)")
    Page<com.student.management.entity.OperationLog> findByConditions(
            @Param("operator") String operator,
            @Param("module") String module,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("isSuccess") Boolean isSuccess,
            Pageable pageable);

    @Query("SELECT DISTINCT o.module FROM com.student.management.entity.OperationLog o ORDER BY o.module")
    List<String> findAllModules();
}
