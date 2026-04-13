package com.dance.me.enrollment.dto;

import java.time.LocalDate;

import com.dance.me.enrollment.entity.EnrollmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long groupId;
    private String groupName;
    private LocalDate enrollmentDate;
    private EnrollmentStatus status;
    private String notes;
}
