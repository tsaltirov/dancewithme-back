package com.dance.me.payment.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.payment.dto.FeePaymentRequest;
import com.dance.me.payment.dto.FeePaymentResponse;
import com.dance.me.payment.entity.FeePayment;
import com.dance.me.payment.entity.PaymentMethod;
import com.dance.me.payment.entity.PaymentStatus;
import com.dance.me.payment.mapper.FeePaymentMapper;
import com.dance.me.payment.repository.FeePaymentRepository;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;
import com.dance.me.student.entity.Student;
import com.dance.me.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeePaymentService {

    private final FeePaymentRepository feePaymentRepository;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final FeePaymentMapper feePaymentMapper;

    public List<FeePaymentResponse> findByStudentId(Long studentId) {
        return feePaymentRepository.findByStudentId(studentId).stream()
                .map(feePaymentMapper::toResponse)
                .toList();
    }

    public List<FeePaymentResponse> findBySchoolAndMonth(Long schoolId, Integer year, Integer month) {
        return feePaymentRepository.findBySchoolIdAndYearAndMonth(schoolId, year, month).stream()
                .map(feePaymentMapper::toResponse)
                .toList();
    }

    public List<FeePaymentResponse> findPending(Long schoolId) {
        List<PaymentStatus> pending = List.of(PaymentStatus.PENDIENTE, PaymentStatus.PARCIAL);
        return feePaymentRepository.findBySchoolIdAndStatusIn(schoolId, pending).stream()
                .map(feePaymentMapper::toResponse)
                .toList();
    }

    @Transactional
    public FeePaymentResponse create(FeePaymentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));
        FeePayment payment = feePaymentMapper.toEntity(request, student, school);
        return feePaymentMapper.toResponse(feePaymentRepository.save(payment));
    }

    @Transactional
    public FeePaymentResponse markAsPaid(Long id, PaymentMethod paymentMethod) {
        FeePayment payment = feePaymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
        payment.setStatus(PaymentStatus.PAGADO);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(paymentMethod);
        return feePaymentMapper.toResponse(feePaymentRepository.save(payment));
    }
}
