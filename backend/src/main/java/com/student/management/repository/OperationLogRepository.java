package com.student.management.repository;

import com.student.management.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    @Query("SELECT o FROM OperationLog o WHERE " +
           "(:startTime IS NULL OR o.operationTime >= :startTime) AND " +
           "(:endTime IS NULL OR o.operationTime <= :endTime) AND " +
           "(:operator IS NULL OR o.operator LIKE CONCAT('%', :operator, '%')) AND " +
           "(:module IS NULL OR o.module LIKE CONCAT('%', :module, '%'))")
    Page<OperationLog> findByConditions(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("operator") String operator,
            @Param("module") String module,
            Pageable pageable);
}
