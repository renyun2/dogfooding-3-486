package com.student.management.mapper;

import com.student.management.dto.CourseDTO;
import com.student.management.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDTO toDTO(Course course);

    List<CourseDTO> toDTOList(List<Course> courses);

    @Mapping(target = "grades", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Course toEntity(CourseDTO dto);
}
