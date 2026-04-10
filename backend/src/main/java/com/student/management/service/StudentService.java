package com.student.management.service;

import com.student.management.entity.Student;
import com.student.management.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        log.info("Fetching student with id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found with id: {}", id);
                    return new RuntimeException("学生不存在，ID: " + id);
                });
    }

    @Transactional
    public Student createStudent(Student student) {
        log.info("Creating new student: {}", student.getName());

        if (studentRepository.existsByEmail(student.getEmail())) {
            log.error("Email already exists: {}", student.getEmail());
            throw new RuntimeException("邮箱已存在: " + student.getEmail());
        }

        Student saved = studentRepository.save(student);
        log.info("Student created successfully with id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Student updateStudent(Long id, Student student) {
        log.info("Updating student with id: {}", id);

        Student existing = getStudentById(id);

        // Check email uniqueness if changed
        if (!existing.getEmail().equals(student.getEmail())
                && studentRepository.existsByEmail(student.getEmail())) {
            log.error("Email already exists: {}", student.getEmail());
            throw new RuntimeException("邮箱已存在: " + student.getEmail());
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

    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);
        Student student = getStudentById(id);
        studentRepository.delete(student);
        log.info("Student deleted successfully with id: {}", id);
    }

    public List<Student> searchStudents(String name) {
        log.info("Searching students with name containing: {}", name);
        return studentRepository.findByNameContaining(name);
    }

    public long getTotalCount() {
        log.info("Fetching total student count");
        return studentRepository.count();
    }
}
