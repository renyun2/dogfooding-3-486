package com.student.management.mapper;

import com.student.management.dto.StudentDTO;
import com.student.management.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    @Mapping(target = "clazzName", source = "clazz.name")
    StudentDTO toDTO(Student student);

    List<StudentDTO> toDTOList(List<Student> students);

    Student toEntity(StudentDTO dto);
}
