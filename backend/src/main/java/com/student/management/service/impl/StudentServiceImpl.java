package com.student.management.service.impl;

import com.student.management.entity.Student;
import com.student.management.exception.BusinessException;
import com.student.management.exception.ResourceNotFoundException;
import com.student.management.repository.StudentRepository;
import com.student.management.service.IStudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

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
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在，ID: " + id));
    }

    @Override
    @Transactional
    public Student createStudent(Student student) {
        log.info("Creating new student: {}", student.getName());

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new BusinessException("邮箱已存在: " + student.getEmail());
        }

        Student saved = studentRepository.save(student);
        log.info("Student created successfully with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Student updateStudent(Long id, Student student) {
        log.info("Updating student with id: {}", id);

        Student existing = getStudentById(id);

        if (!existing.getEmail().equals(student.getEmail())
                && studentRepository.existsByEmail(student.getEmail())) {
            throw new BusinessException("邮箱已存在: " + student.getEmail());
        }

        existing.setName(student.getName());
        existing.setGender(student.getGender());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());
        existing.setPhone(student.getPhone());
        existing.setEnrollmentDate(student.getEnrollmentDate());

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
}
