package com.dance.me.costume.controller;

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
import com.dance.me.costume.dto.CostumeRequest;
import com.dance.me.costume.dto.CostumeResponse;
import com.dance.me.costume.service.CostumeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/costumes")
@RequiredArgsConstructor
public class CostumeController {

    private final CostumeService costumeService;

    @GetMapping("/participation/{participationId}")
    public ResponseEntity<ApiResponse<List<CostumeResponse>>> getByParticipationId(@PathVariable Long participationId) {
        return ResponseEntity.ok(ApiResponse.ok(costumeService.findByParticipationId(participationId)));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<CostumeResponse>>> getPendingReturn() {
        return ResponseEntity.ok(ApiResponse.ok(costumeService.findPendingReturn()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CostumeResponse>> create(@Validated @RequestBody CostumeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Vestuario registrado", costumeService.create(request)));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<ApiResponse<CostumeResponse>> markAsReturned(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Vestuario devuelto", costumeService.markAsReturned(id)));
    }
}
