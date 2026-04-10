package com.student.management.mapper;

import com.student.management.dto.ClazzDTO;
import com.student.management.entity.Clazz;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClazzMapper {

    ClazzDTO toDTO(Clazz clazz);

    List<ClazzDTO> toDTOList(List<Clazz> clazzes);

    Clazz toEntity(ClazzDTO dto);
}
