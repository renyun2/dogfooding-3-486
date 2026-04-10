package com.student.management.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDTO {

    private Long id;

    private String name;

    private String studentNo;

    private String gender;

    private LocalDate birthday;

    private String email;

    private String phone;

    private Long clazzId;

    private String clazzName;
}
