package com.dance.me.event.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.event.dto.EventRequest;
import com.dance.me.event.dto.EventResponse;
import com.dance.me.event.dto.ParticipationRequest;
import com.dance.me.event.dto.ParticipationResponse;
import com.dance.me.event.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getBySchoolId(@PathVariable Long schoolId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.findBySchoolId(schoolId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> create(@Validated @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Evento creado", eventService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> update(@PathVariable Long id, @Validated @RequestBody EventRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Evento actualizado", eventService.update(id, request)));
    }

    @GetMapping("/{eventId}/participations")
    public ResponseEntity<ApiResponse<List<ParticipationResponse>>> getParticipations(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.findParticipationsByEventId(eventId)));
    }

    @PostMapping("/participations")
    public ResponseEntity<ApiResponse<ParticipationResponse>> addParticipation(@Validated @RequestBody ParticipationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Participación registrada", eventService.addParticipation(request)));
    }
}
