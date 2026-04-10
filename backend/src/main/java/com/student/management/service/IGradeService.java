package com.student.management.service;

import com.student.management.dto.GradeDTO;
import com.student.management.entity.Grade;

import java.util.List;
import java.util.Map;

/**
 * 成绩服务接口
 */
public interface IGradeService {

    /**
     * 获取所有成绩
     */
    List<Grade> getAllGrades();

    /**
     * 根据ID获取成绩
     */
    Grade getGradeById(Long id);

    /**
     * 获取学生的所有成绩
     */
    List<Grade> getGradesByStudentId(Long studentId);

    /**
     * 获取课程的所有成绩
     */
    List<Grade> getGradesByCourseId(Long courseId);

    /**
     * 创建成绩
     */
    Grade createGrade(GradeDTO gradeDTO);

    /**
     * 更新成绩
     */
    Grade updateGrade(Long id, GradeDTO gradeDTO);

    /**
     * 删除成绩
     */
    void deleteGrade(Long id);

    /**
     * 获取成绩统计
     */
    Map<String, Object> getStatistics();

    /**
     * 获取学生平均分
     */
    Double getAverageScoreByStudent(Long studentId);

    /**
     * 获取课程平均分
     */
    Double getAverageScoreByCourse(Long courseId);
}
