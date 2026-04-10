package com.student.management.service;

import com.student.management.entity.Teacher;
import com.student.management.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> getAllTeachers() {
        log.info("Fetching all teachers");
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        log.info("Fetching teacher with id: {}", id);
        return teacherRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Teacher not found with id: {}", id);
                    return new RuntimeException("教师不存在，ID: " + id);
                });
    }

    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        log.info("Creating new teacher: {}", teacher.getName());

        if (teacherRepository.existsByTeacherNo(teacher.getTeacherNo())) {
            log.error("TeacherNo already exists: {}", teacher.getTeacherNo());
            throw new RuntimeException("工号已存在: " + teacher.getTeacherNo());
        }

        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            log.error("Email already exists: {}", teacher.getEmail());
            throw new RuntimeException("邮箱已存在: " + teacher.getEmail());
        }

        Teacher saved = teacherRepository.save(teacher);
        log.info("Teacher created successfully with id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacher) {
        log.info("Updating teacher with id: {}", id);

        Teacher existing = getTeacherById(id);

        if (!existing.getTeacherNo().equals(teacher.getTeacherNo())
                && teacherRepository.existsByTeacherNo(teacher.getTeacherNo())) {
            log.error("TeacherNo already exists: {}", teacher.getTeacherNo());
            throw new RuntimeException("工号已存在: " + teacher.getTeacherNo());
        }

        if (!existing.getEmail().equals(teacher.getEmail())
                && teacherRepository.existsByEmail(teacher.getEmail())) {
            log.error("Email already exists: {}", teacher.getEmail());
            throw new RuntimeException("邮箱已存在: " + teacher.getEmail());
        }

        existing.setTeacherNo(teacher.getTeacherNo());
        existing.setName(teacher.getName());
        existing.setGender(teacher.getGender());
        existing.setTitle(teacher.getTitle());
        existing.setDepartment(teacher.getDepartment());
        existing.setPhone(teacher.getPhone());
        existing.setEmail(teacher.getEmail());
        existing.setHireDate(teacher.getHireDate());

        Teacher updated = teacherRepository.save(existing);
        log.info("Teacher updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Transactional
    public void deleteTeacher(Long id) {
        log.info("Deleting teacher with id: {}", id);
        Teacher teacher = getTeacherById(id);
        teacherRepository.delete(teacher);
        log.info("Teacher deleted successfully with id: {}", id);
    }

    public List<Teacher> searchTeachers(String name) {
        log.info("Searching teachers with name containing: {}", name);
        return teacherRepository.findByNameContaining(name);
    }

    public long getTotalCount() {
        log.info("Fetching total teacher count");
        return teacherRepository.count();
    }
}
