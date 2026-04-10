package com.student.management.dto.converter;

import com.student.management.dto.ClazzDTO;
import com.student.management.entity.Clazz;
import org.springframework.beans.BeanUtils;

public class ClazzConverter {

    private ClazzConverter() {
    }

    public static ClazzDTO toDTO(Clazz clazz) {
        if (clazz == null) {
            return null;
        }
        ClazzDTO dto = new ClazzDTO();
        BeanUtils.copyProperties(clazz, dto);
        return dto;
    }

    public static Clazz toEntity(ClazzDTO dto) {
        if (dto == null) {
            return null;
        }
        Clazz clazz = new Clazz();
        BeanUtils.copyProperties(dto, clazz);
        return clazz;
    }
}
