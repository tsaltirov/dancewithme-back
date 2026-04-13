package com.dance.me.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.payment.dto.FeePaymentRequest;
import com.dance.me.payment.dto.FeePaymentResponse;
import com.dance.me.payment.entity.PaymentMethod;
import com.dance.me.payment.service.FeePaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class FeePaymentController {

    private final FeePaymentService feePaymentService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<FeePaymentResponse>>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.ok(feePaymentService.findByStudentId(studentId)));
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<ApiResponse<List<FeePaymentResponse>>> getBySchoolAndMonth(
            @PathVariable Long schoolId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return ResponseEntity.ok(ApiResponse.ok(feePaymentService.findBySchoolAndMonth(schoolId, year, month)));
    }

    @GetMapping("/school/{schoolId}/pending")
    public ResponseEntity<ApiResponse<List<FeePaymentResponse>>> getPending(@PathVariable Long schoolId) {
        return ResponseEntity.ok(ApiResponse.ok(feePaymentService.findPending(schoolId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FeePaymentResponse>> create(@Validated @RequestBody FeePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pago registrado", feePaymentService.create(request)));
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<FeePaymentResponse>> markAsPaid(
            @PathVariable Long id,
            @RequestParam PaymentMethod paymentMethod) {
        return ResponseEntity.ok(ApiResponse.ok("Pago marcado como pagado", feePaymentService.markAsPaid(id, paymentMethod)));
    }
}
