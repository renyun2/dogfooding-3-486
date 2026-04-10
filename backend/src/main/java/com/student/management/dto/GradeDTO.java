package com.student.management.dto;

import lombok.Data;

@Data
public class GradeDTO {

    private Long id;

    private Long studentId;

    private String studentName;

    private Long courseId;

    private String courseName;

    private Double score;
}
