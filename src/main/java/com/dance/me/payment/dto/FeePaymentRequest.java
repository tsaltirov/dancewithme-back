package com.dance.me.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dance.me.payment.entity.PaymentMethod;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePaymentRequest {

    @NotNull(message = "El alumno es obligatorio")
    private Long studentId;

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;

    @NotNull(message = "El año es obligatorio")
    private Integer year;

    @NotNull(message = "El mes es obligatorio")
    @Min(value = 1, message = "El mes debe estar entre 1 y 12")
    @Max(value = 12, message = "El mes debe estar entre 1 y 12")
    private Integer month;

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser positivo")
    private BigDecimal amount;

    private LocalDate paymentDate;

    private PaymentMethod paymentMethod;

    private String notes;
}
