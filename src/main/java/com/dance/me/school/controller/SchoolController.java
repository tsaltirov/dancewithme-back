package com.dance.me.school.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.school.dto.SchoolRequest;
import com.dance.me.school.dto.SchoolResponse;
import com.dance.me.school.service.SchoolService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SchoolResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(schoolService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchoolResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(schoolService.findById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<SchoolResponse>>> getByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(schoolService.findByUserId(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SchoolResponse>> create(@Validated @RequestBody SchoolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Escuela creada", schoolService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SchoolResponse>> update(@PathVariable Long id, @Validated @RequestBody SchoolRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Escuela actualizada", schoolService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        schoolService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok("Escuela desactivada", null));
    }
}
