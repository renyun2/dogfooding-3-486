package com.student.management.service;

import com.student.management.entity.Grade;

import java.util.List;
import java.util.Map;

public interface IGradeService {

    List<Grade> getAllGrades();

    Grade getGradeById(Long id);

    List<Grade> getGradesByStudentId(Long studentId);

    List<Grade> getGradesByCourseId(Long courseId);

    Grade createGrade(Grade grade);

    Grade updateGrade(Long id, Grade grade);

    void deleteGrade(Long id);

    Map<String, Object> getStatistics();

    Double getAverageScoreByStudent(Long studentId);

    Double getAverageScoreByCourse(Long courseId);
}
