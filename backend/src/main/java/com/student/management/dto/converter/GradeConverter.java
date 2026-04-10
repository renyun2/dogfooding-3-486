package com.student.management.dto.converter;

import com.student.management.dto.GradeDTO;
import com.student.management.entity.Grade;
import org.springframework.beans.BeanUtils;

public class GradeConverter {

    private GradeConverter() {
    }

    public static GradeDTO toDTO(Grade grade) {
        if (grade == null) {
            return null;
        }
        GradeDTO dto = new GradeDTO();
        BeanUtils.copyProperties(grade, dto);
        return dto;
    }

    public static Grade toEntity(GradeDTO dto) {
        if (dto == null) {
            return null;
        }
        Grade grade = new Grade();
        BeanUtils.copyProperties(dto, grade);
        return grade;
    }
}
