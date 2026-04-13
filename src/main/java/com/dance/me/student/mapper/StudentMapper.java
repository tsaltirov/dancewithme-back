package com.dance.me.student.mapper;

import org.springframework.stereotype.Component;

import com.dance.me.school.entity.School;
import com.dance.me.student.dto.StudentRequest;
import com.dance.me.student.dto.StudentResponse;
import com.dance.me.student.entity.Student;

@Component
public class StudentMapper {

    public StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .birthDate(student.getBirthDate())
                .schoolId(student.getSchool().getId())
                .schoolName(student.getSchool().getName())
                .enrollmentDate(student.getEnrollmentDate())
                .active(student.getActive())
                .build();
    }

    public Student toEntity(StudentRequest request, School school) {
        return Student.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .school(school)
                .build();
    }
}
