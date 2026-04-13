package com.dance.me.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dance.me.payment.entity.PaymentMethod;
import com.dance.me.payment.entity.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePaymentResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long schoolId;
    private String schoolName;
    private Integer year;
    private Integer month;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private String notes;
    private LocalDateTime createdAt;
}
