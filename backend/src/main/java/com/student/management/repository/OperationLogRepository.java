package com.student.management.repository;

import com.student.management.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    @Query("SELECT o FROM OperationLog o WHERE " +
           "(:startTime IS NULL OR o.operationTime >= :startTime) AND " +
           "(:endTime IS NULL OR o.operationTime <= :endTime) AND " +
           "(:operator IS NULL OR o.operator LIKE %:operator%) AND " +
           "(:module IS NULL OR o.operationModule LIKE %:module%) " +
           "ORDER BY o.operationTime DESC")
    Page<OperationLog> findByConditions(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("operator") String operator,
            @Param("module") String module,
            Pageable pageable);

    List<OperationLog> findTop50ByOrderByOperationTimeDesc();
}
