package com.dance.me.payment.mapper;

import org.springframework.stereotype.Component;

import com.dance.me.payment.dto.FeePaymentRequest;
import com.dance.me.payment.dto.FeePaymentResponse;
import com.dance.me.payment.entity.FeePayment;
import com.dance.me.school.entity.School;
import com.dance.me.student.entity.Student;

@Component
public class FeePaymentMapper {

    public FeePaymentResponse toResponse(FeePayment payment) {
        Student student = payment.getStudent();
        School school = payment.getSchool();
        return FeePaymentResponse.builder()
                .id(payment.getId())
                .studentId(student.getId())
                .studentName(student.getName() + " " + student.getLastName())
                .schoolId(school.getId())
                .schoolName(school.getName())
                .year(payment.getYear())
                .month(payment.getMonth())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .notes(payment.getNotes())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public FeePayment toEntity(FeePaymentRequest request, Student student, School school) {
        return FeePayment.builder()
                .student(student)
                .school(school)
                .year(request.getYear())
                .month(request.getMonth())
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .paymentMethod(request.getPaymentMethod())
                .notes(request.getNotes())
                .build();
    }
}
