package com.dance.me.enrollment.mapper;

import org.springframework.stereotype.Component;

import com.dance.me.enrollment.dto.EnrollmentResponse;
import com.dance.me.enrollment.entity.Enrollment;
import com.dance.me.group.entity.DanceGroup;
import com.dance.me.student.entity.Student;

@Component
public class EnrollmentMapper {

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        Student student = enrollment.getStudent();
        DanceGroup group = enrollment.getGroup();
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(student.getId())
                .studentName(student.getName() + " " + student.getLastName())
                .groupId(group.getId())
                .groupName(group.getName())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .notes(enrollment.getNotes())
                .build();
    }

    public Enrollment toEntity(Student student, DanceGroup group, String notes) {
        return Enrollment.builder()
                .student(student)
                .group(group)
                .notes(notes)
                .build();
    }
}
