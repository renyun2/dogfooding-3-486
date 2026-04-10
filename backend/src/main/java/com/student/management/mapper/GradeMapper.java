package com.student.management.mapper;

import com.student.management.dto.GradeDTO;
import com.student.management.entity.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GradeMapper {

    @Mapping(target = "studentName", ignore = true)
    @Mapping(target = "courseName", ignore = true)
    GradeDTO toDTO(Grade grade);

    List<GradeDTO> toDTOList(List<Grade> grades);

    Grade toEntity(GradeDTO dto);
}
