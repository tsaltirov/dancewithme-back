package com.dance.me.costume.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.costume.dto.CostumeRequest;
import com.dance.me.costume.dto.CostumeResponse;
import com.dance.me.costume.dto.EventCostumeRequest;
import com.dance.me.costume.dto.EventCostumeResponse;
import com.dance.me.costume.service.CostumeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/costumes")
@RequiredArgsConstructor
public class CostumeController {

    private final CostumeService costumeService;

    // ── Catálogo ──────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<CostumeResponse>>> getBySchool(
            @RequestParam Long schoolId,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(ApiResponse.ok(costumeService.findBySchool(schoolId, active)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CostumeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(costumeService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CostumeResponse>> create(@Valid @RequestBody CostumeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Vestuario creado", costumeService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CostumeResponse>> update(
            @PathVariable Long id, @Valid @RequestBody CostumeRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Vestuario actualizado", costumeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        costumeService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok("Vestuario desactivado", null));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CostumeResponse>> activate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Vestuario activado", costumeService.activate(id)));
    }

    // ── Asignaciones a eventos ────────────────────────────────────────────────

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<EventCostumeResponse>> assign(
            @Valid @RequestBody EventCostumeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Vestuario asignado", costumeService.assign(request)));
    }

    @GetMapping("/assignments/participation/{participationId}")
    public ResponseEntity<ApiResponse<List<EventCostumeResponse>>> getAssignmentsByParticipation(
            @PathVariable Long participationId) {
        return ResponseEntity.ok(ApiResponse.ok(costumeService.findAssignmentsByParticipation(participationId)));
    }

    @GetMapping("/assignments/pending")
    public ResponseEntity<ApiResponse<List<EventCostumeResponse>>> getPendingReturn() {
        return ResponseEntity.ok(ApiResponse.ok(costumeService.findPendingReturn()));
    }

    @PatchMapping("/assignments/{id}/return")
    public ResponseEntity<ApiResponse<EventCostumeResponse>> markAsReturned(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Vestuario devuelto", costumeService.markAsReturned(id)));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable Long id) {
        costumeService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponse.ok("Asignación eliminada", null));
    }
}
