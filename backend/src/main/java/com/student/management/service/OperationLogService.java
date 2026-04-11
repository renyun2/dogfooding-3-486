package com.student.management.service;

import com.student.management.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    @Transactional
    public void saveLog(com.student.management.entity.OperationLog log) {
        operationLogRepository.save(log);
    }

    public Page<com.student.management.entity.OperationLog> getLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findAll(pageable);
    }

    public com.student.management.entity.OperationLog getLogById(Long id) {
        return operationLogRepository.findById(id).orElse(null);
    }

    public Page<com.student.management.entity.OperationLog> searchLogs(String operator, String module, 
                                         LocalDateTime startTime, LocalDateTime endTime,
                                         Boolean isSuccess, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return operationLogRepository.findByConditions(operator, module, startTime, endTime, isSuccess, pageable);
    }

    public List<String> getAllModules() {
        return operationLogRepository.findAllModules();
    }

    @Transactional
    public void deleteLogsBeforeTime(LocalDateTime time) {
        List<com.student.management.entity.OperationLog> logsToDelete = operationLogRepository.findAll()
                .stream()
                .filter(log -> log.getOperationTime().isBefore(time))
                .toList();
        operationLogRepository.deleteAll(logsToDelete);
    }
}
