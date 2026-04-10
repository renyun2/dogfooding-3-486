package com.student.management.service;

import com.student.management.entity.Clazz;

import java.util.List;

public interface IClazzService {

    List<Clazz> getAllClasses();

    Clazz getClassById(Long id);

    Clazz createClass(Clazz clazz);

    Clazz updateClass(Long id, Clazz clazz);

    void deleteClass(Long id);

    long getTotalCount();
}
