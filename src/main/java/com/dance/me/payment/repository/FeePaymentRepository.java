package com.dance.me.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dance.me.payment.entity.FeePayment;
import com.dance.me.payment.entity.PaymentStatus;

@Repository
public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {

    List<FeePayment> findByStudentId(Long studentId);

    List<FeePayment> findBySchoolIdAndYearAndMonth(Long schoolId, Integer year, Integer month);

    List<FeePayment> findByStudentIdAndStatusIn(Long studentId, List<PaymentStatus> statuses);

    List<FeePayment> findBySchoolIdAndStatusIn(Long schoolId, List<PaymentStatus> statuses);
}
