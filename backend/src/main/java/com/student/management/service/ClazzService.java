package com.student.management.service;

import com.student.management.dto.ClazzDTO;
import com.student.management.entity.Clazz;
import com.student.management.exception.DuplicateResourceException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.ClazzRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 班级服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClazzService implements IClazzService {

    private final ClazzRepository clazzRepository;

    @Override
    public List<Clazz> getAllClasses() {
        log.info("Fetching all classes");
        return clazzRepository.findAll();
    }

    @Override
    public Clazz getClassById(Long id) {
        log.info("Fetching class with id: {}", id);
        return clazzRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Class not found with id: {}", id);
                    return new ResourceNotFoundException("班级", id);
                });
    }

    @Override
    @Transactional
    public Clazz createClass(ClazzDTO clazzDTO) {
        log.info("Creating new class: {}", clazzDTO.getName());

        if (clazzRepository.existsByName(clazzDTO.getName())) {
            log.error("Class name already exists: {}", clazzDTO.getName());
            throw new DuplicateResourceException("班级名称", "name", clazzDTO.getName());
        }

        Clazz clazz = convertToEntity(clazzDTO);
        Clazz saved = clazzRepository.save(clazz);
        log.info("Class created successfully with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Clazz updateClass(Long id, ClazzDTO clazzDTO) {
        log.info("Updating class with id: {}", id);

        Clazz existing = getClassById(id);

        if (!existing.getName().equals(clazzDTO.getName())
                && clazzRepository.existsByName(clazzDTO.getName())) {
            log.error("Class name already exists: {}", clazzDTO.getName());
            throw new DuplicateResourceException("班级名称", "name", clazzDTO.getName());
        }

        updateEntityFromDTO(existing, clazzDTO);

        Clazz updated = clazzRepository.save(existing);
        log.info("Class updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Override
    @Transactional
    public void deleteClass(Long id) {
        log.info("Deleting class with id: {}", id);
        Clazz clazz = getClassById(id);
        clazzRepository.delete(clazz);
        log.info("Class and its students/grades deleted successfully with id: {}", id);
    }

    @Override
    public long getTotalCount() {
        return clazzRepository.count();
    }

    /**
     * 将DTO转换为实体
     */
    private Clazz convertToEntity(ClazzDTO dto) {
        Clazz clazz = new Clazz();
        clazz.setName(dto.getName());
        clazz.setDescription(dto.getDescription());
        return clazz;
    }

    /**
     * 使用DTO更新实体
     */
    private void updateEntityFromDTO(Clazz clazz, ClazzDTO dto) {
        clazz.setName(dto.getName());
        clazz.setDescription(dto.getDescription());
    }
}
