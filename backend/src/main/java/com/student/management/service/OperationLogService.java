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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    public Page<OperationLog> getOperationLogs(int page, int size, LocalDateTime startTime, LocalDateTime endTime, String operator, String module) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByConditions(startTime, endTime, operator, module, pageable);
    }

    public OperationLog getOperationLogById(Long id) {
        Optional<OperationLog> log = operationLogRepository.findById(id);
        return log.orElseThrow(() -> new RuntimeException("操作日志不存在"));
    }

    public void deleteOperationLog(Long id) {
        if (!operationLogRepository.existsById(id)) {
            throw new RuntimeException("操作日志不存在");
        }
        operationLogRepository.deleteById(id);
    }
}
