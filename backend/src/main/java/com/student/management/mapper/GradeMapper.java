package com.student.management.mapper;

import com.student.management.dto.GradeDTO;
import com.student.management.entity.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    @Mapping(target = "studentName", source = "student.name")
    @Mapping(target = "courseName", source = "course.name")
    GradeDTO toDTO(Grade grade);

    List<GradeDTO> toDTOList(List<Grade> grades);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Grade toEntity(GradeDTO dto);
}
