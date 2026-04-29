package com.dance.me.costume.controller;

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
import com.dance.me.costume.dto.BoxRequest;
import com.dance.me.costume.dto.BoxResponse;
import com.dance.me.costume.service.BoxService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/boxes")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;

    // ── Catálogo de cajas ─────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<BoxResponse>>> getBySchool(@RequestParam Long schoolId) {
        return ResponseEntity.ok(ApiResponse.ok(boxService.findBySchool(schoolId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoxResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(boxService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BoxResponse>> create(@Valid @RequestBody BoxRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Caja creada", boxService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BoxResponse>> update(
            @PathVariable Long id, @Valid @RequestBody BoxRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Caja actualizada", boxService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        boxService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok("Caja desactivada", null));
    }

    // ── Costumes dentro de una caja ───────────────────────────────────────────

    @PostMapping("/{boxId}/costumes/{costumeId}")
    public ResponseEntity<ApiResponse<BoxResponse>> addCostume(
            @PathVariable Long boxId, @PathVariable Long costumeId) {
        return ResponseEntity.ok(ApiResponse.ok("Vestuario añadido a la caja",
                boxService.addCostume(boxId, costumeId)));
    }

    @DeleteMapping("/{boxId}/costumes/{costumeId}")
    public ResponseEntity<ApiResponse<Void>> removeCostume(
            @PathVariable Long boxId, @PathVariable Long costumeId) {
        boxService.removeCostume(boxId, costumeId);
        return ResponseEntity.ok(ApiResponse.ok("Vestuario eliminado de la caja", null));
    }

    // ── Cajas asignadas a eventos ─────────────────────────────────────────────

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<List<BoxResponse>>> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.ok(boxService.findByEvent(eventId)));
    }

    @PostMapping("/{boxId}/events/{eventId}")
    public ResponseEntity<ApiResponse<Void>> assignToEvent(
            @PathVariable Long boxId, @PathVariable Long eventId) {
        boxService.assignToEvent(boxId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Caja asignada al evento", null));
    }

    @DeleteMapping("/{boxId}/events/{eventId}")
    public ResponseEntity<ApiResponse<Void>> removeFromEvent(
            @PathVariable Long boxId, @PathVariable Long eventId) {
        boxService.removeFromEvent(boxId, eventId);
        return ResponseEntity.ok(ApiResponse.ok("Caja desasignada del evento", null));
    }
}
