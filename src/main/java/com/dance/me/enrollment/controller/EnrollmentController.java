package com.dance.me.enrollment.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.enrollment.dto.EnrollmentRequest;
import com.dance.me.enrollment.dto.EnrollmentResponse;
import com.dance.me.enrollment.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.ok(enrollmentService.findByStudentId(studentId)));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByGroupId(@PathVariable Long groupId) {
        return ResponseEntity.ok(ApiResponse.ok(enrollmentService.findByGroupId(groupId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> create(@Validated @RequestBody EnrollmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Inscripción creada", enrollmentService.create(request)));
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> withdraw(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Inscripción dada de baja", enrollmentService.withdraw(id)));
    }
}
