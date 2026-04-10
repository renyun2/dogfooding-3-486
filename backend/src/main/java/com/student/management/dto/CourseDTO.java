package com.student.management.dto;

import lombok.Data;

@Data
public class CourseDTO {

    private Long id;

    private String name;

    private Integer credits;

    private String instructor;

    private String description;
}
