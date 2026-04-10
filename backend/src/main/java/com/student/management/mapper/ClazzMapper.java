package com.student.management.mapper;

import com.student.management.dto.ClazzDTO;
import com.student.management.entity.Clazz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClazzMapper {

    ClazzDTO toDTO(Clazz clazz);

    List<ClazzDTO> toDTOList(List<Clazz> clazzes);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Clazz toEntity(ClazzDTO dto);
}
