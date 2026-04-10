package com.student.management.service;

import com.student.management.entity.Clazz;
import com.student.management.exception.BusinessException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.ClazzRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClazzService implements IClazzService {

    private final ClazzRepository clazzRepository;

    public List<Clazz> getAllClasses() {
        log.info("Fetching all classes");
        return clazzRepository.findAll();
    }

    public Clazz getClassById(Long id) {
        log.info("Fetching class with id: {}", id);
        return clazzRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Class not found with id: {}", id);
                    return new ResourceNotFoundException("班级不存在，ID: " + id);
                });
    }

    @Transactional
    public Clazz createClass(Clazz clazz) {
        log.info("Creating new class: {}", clazz.getName());
        if (clazzRepository.existsByName(clazz.getName())) {
            log.error("Class name already exists: {}", clazz.getName());
            throw new BusinessException("班级名称已存在: " + clazz.getName());
        }
        Clazz saved = clazzRepository.save(clazz);
        log.info("Class created successfully with id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Clazz updateClass(Long id, Clazz clazz) {
        log.info("Updating class with id: {}", id);
        Clazz existing = getClassById(id);

        if (!existing.getName().equals(clazz.getName()) && clazzRepository.existsByName(clazz.getName())) {
            log.error("Class name already exists: {}", clazz.getName());
            throw new BusinessException("班级名称已存在: " + clazz.getName());
        }

        existing.setName(clazz.getName());
        existing.setDescription(clazz.getDescription());

        Clazz updated = clazzRepository.save(existing);
        log.info("Class updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Transactional
    public void deleteClass(Long id) {
        log.info("Deleting class with id: {}", id);
        Clazz clazz = getClassById(id);
        clazzRepository.delete(clazz);
        log.info("Class and its students/grades deleted successfully with id: {}", id);
    }

    public long getTotalCount() {
        return clazzRepository.count();
    }
}
