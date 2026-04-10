package com.student.management.dto.converter;

import com.student.management.dto.CourseDTO;
import com.student.management.entity.Course;
import org.springframework.beans.BeanUtils;

public class CourseConverter {

    private CourseConverter() {
    }

    public static CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }
        CourseDTO dto = new CourseDTO();
        BeanUtils.copyProperties(course, dto);
        return dto;
    }

    public static Course toEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        return course;
    }
}
