package com.student.management.service;

import com.student.management.entity.OperationLog;
import com.student.management.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    public Page<OperationLog> getOperationLogs(
            LocalDateTime startTime,
            LocalDateTime endTime,
            String operator,
            String module,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByConditions(startTime, endTime, operator, module, pageable);
    }

    public List<OperationLog> getRecentLogs() {
        return operationLogRepository.findTop50ByOrderByOperationTimeDesc();
    }

    public OperationLog getLogById(Long id) {
        return operationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("日志记录不存在"));
    }
}
