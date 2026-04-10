package com.student.management.service;

import com.student.management.dto.StudentDTO;
import com.student.management.entity.Student;
import com.student.management.exception.DuplicateResourceException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学生服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        log.info("Fetching student with id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found with id: {}", id);
                    return new ResourceNotFoundException("学生", id);
                });
    }

    @Override
    @Transactional
    public Student createStudent(StudentDTO studentDTO) {
        log.info("Creating new student: {}", studentDTO.getName());

        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            log.error("Email already exists: {}", studentDTO.getEmail());
            throw new DuplicateResourceException("邮箱", "email", studentDTO.getEmail());
        }

        Student student = convertToEntity(studentDTO);
        Student saved = studentRepository.save(student);
        log.info("Student created successfully with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with id: {}", id);

        Student existing = getStudentById(id);

        // Check email uniqueness if changed
        if (!existing.getEmail().equals(studentDTO.getEmail())
                && studentRepository.existsByEmail(studentDTO.getEmail())) {
            log.error("Email already exists: {}", studentDTO.getEmail());
            throw new DuplicateResourceException("邮箱", "email", studentDTO.getEmail());
        }

        updateEntityFromDTO(existing, studentDTO);

        Student updated = studentRepository.save(existing);
        log.info("Student updated successfully with id: {}", updated.getId());
        return updated;
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);
        Student student = getStudentById(id);
        studentRepository.delete(student);
        log.info("Student deleted successfully with id: {}", id);
    }

    @Override
    public List<Student> searchStudents(String name) {
        log.info("Searching students with name containing: {}", name);
        return studentRepository.findByNameContaining(name);
    }

    @Override
    public long getTotalCount() {
        log.info("Fetching total student count");
        return studentRepository.count();
    }

    /**
     * 将DTO转换为实体
     */
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setClazzId(dto.getClazzId());
        return student;
    }

    /**
     * 使用DTO更新实体
     */
    private void updateEntityFromDTO(Student student, StudentDTO dto) {
        student.setName(dto.getName());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setClazzId(dto.getClazzId());
    }
}
