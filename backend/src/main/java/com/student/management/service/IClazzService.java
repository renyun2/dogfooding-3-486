package com.student.management.service;

import com.student.management.dto.ClazzDTO;
import com.student.management.entity.Clazz;

import java.util.List;

/**
 * 班级服务接口
 */
public interface IClazzService {

    /**
     * 获取所有班级
     */
    List<Clazz> getAllClasses();

    /**
     * 根据ID获取班级
     */
    Clazz getClassById(Long id);

    /**
     * 创建班级
     */
    Clazz createClass(ClazzDTO clazzDTO);

    /**
     * 更新班级
     */
    Clazz updateClass(Long id, ClazzDTO clazzDTO);

    /**
     * 删除班级
     */
    void deleteClass(Long id);

    /**
     * 获取班级总数
     */
    long getTotalCount();
}
