package com.dance.me.student.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Long schoolId;
    private String schoolName;
    private LocalDate enrollmentDate;
    private Boolean active;
}
