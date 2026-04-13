package com.dance.me.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dance.me.event.entity.EventPaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationResponse {

    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long studentId;
    private String studentName;
    private LocalDateTime registrationDate;
    private EventPaymentStatus paymentStatus;
    private BigDecimal paidAmount;
}
