package com.student.management.mapper;

import com.student.management.dto.CourseDTO;
import com.student.management.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    CourseDTO toDTO(Course course);

    List<CourseDTO> toDTOList(List<Course> courses);

    Course toEntity(CourseDTO dto);
}
