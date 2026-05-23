package com.dance.me.coreografia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.coreografia.dto.CoreografiaListResponse;
import com.dance.me.coreografia.dto.CoreografiaRequest;
import com.dance.me.coreografia.dto.CoreografiaResponse;
import com.dance.me.coreografia.service.CoreografiaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coreografias")
@RequiredArgsConstructor
public class CoreografiaController {

    private final CoreografiaService coreografiaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CoreografiaListResponse>>> getBySchool(
            @RequestParam Long schoolId) {
        return ResponseEntity.ok(ApiResponse.ok(coreografiaService.findBySchool(schoolId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CoreografiaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(coreografiaService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CoreografiaResponse>> create(
            @Valid @RequestBody CoreografiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Coreografía creada", coreografiaService.create(request)));
    }

    // Guarda el documento completo — reemplaza escenas, posiciones y bailarines
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CoreografiaResponse>> save(
            @PathVariable Long id, @Valid @RequestBody CoreografiaRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Coreografía guardada", coreografiaService.save(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        coreografiaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Coreografía eliminada", null));
    }
}
