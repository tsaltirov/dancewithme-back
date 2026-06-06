package com.dance.me.student.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.student.dto.CsvImportResponse;
import com.dance.me.student.dto.StudentRequest;
import com.dance.me.student.dto.StudentResponse;
import com.dance.me.student.service.StudentCsvService;
import com.dance.me.student.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentCsvService studentCsvService;

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getBySchoolId(@PathVariable Long schoolId) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.findBySchoolId(schoolId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(@Validated @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Alumno creado", studentService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(@PathVariable Long id, @Validated @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Alumno actualizado", studentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        studentService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok("Alumno desactivado", null));
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse<CsvImportResponse>> importFromCsv(
            @RequestParam MultipartFile file,
            @RequestParam Long schoolId) {
        CsvImportResponse result = studentCsvService.importStudents(file, schoolId);
        String message = String.format("Importación completada: %d creados, %d fallidos de %d",
                result.getCreated(), result.getFailed(), result.getTotal());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(message, result));
    }
}
