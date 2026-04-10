package com.student.management.dto.converter;

import com.student.management.dto.StudentDTO;
import com.student.management.entity.Student;
import com.student.management.entity.Clazz;
import org.springframework.beans.BeanUtils;

public class StudentConverter {

    private StudentConverter() {
    }

    public static StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }
        StudentDTO dto = new StudentDTO();
        BeanUtils.copyProperties(student, dto);
        if (student.getClazz() != null) {
            dto.setClazzId(student.getClazz().getId());
            dto.setClazzName(student.getClazz().getName());
        }
        return dto;
    }

    public static Student toEntity(StudentDTO dto) {
        if (dto == null) {
            return null;
        }
        Student student = new Student();
        BeanUtils.copyProperties(dto, student);
        return student;
    }
}
