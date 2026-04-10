package com.student.management.mapper;

import com.student.management.dto.StudentDTO;
import com.student.management.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "clazzName", source = "clazz.name")
    StudentDTO toDTO(Student student);

    List<StudentDTO> toDTOList(List<Student> students);

    @Mapping(target = "clazz", ignore = true)
    @Mapping(target = "grades", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Student toEntity(StudentDTO dto);
}
