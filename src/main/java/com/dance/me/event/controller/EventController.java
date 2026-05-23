package com.dance.me.event.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.event.dto.EventPriceRequest;
import com.dance.me.event.dto.EventPriceResponse;
import com.dance.me.event.dto.EventRequest;
import com.dance.me.event.dto.EventResponse;
import com.dance.me.event.dto.ParticipationRequest;
import com.dance.me.event.dto.ParticipationResponse;
import com.dance.me.event.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // ── Eventos ───────────────────────────────────────────────────────────────

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getBySchoolId(@PathVariable Long schoolId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.findBySchoolId(schoolId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> create(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Evento creado", eventService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> update(
            @PathVariable Long id, @Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Evento actualizado", eventService.update(id, request)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<EventResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Evento cancelado", eventService.cancel(id)));
    }

    // ── Precios ───────────────────────────────────────────────────────────────

    @GetMapping("/{eventId}/prices")
    public ResponseEntity<ApiResponse<List<EventPriceResponse>>> getPrices(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.findPrices(eventId)));
    }

    @PostMapping("/{eventId}/prices")
    public ResponseEntity<ApiResponse<EventPriceResponse>> addPrice(
            @PathVariable Long eventId, @Valid @RequestBody EventPriceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Precio añadido", eventService.addPrice(eventId, request)));
    }

    @PutMapping("/{eventId}/prices/{priceId}")
    public ResponseEntity<ApiResponse<EventPriceResponse>> updatePrice(
            @PathVariable Long eventId, @PathVariable Long priceId,
            @Valid @RequestBody EventPriceRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Precio actualizado",
                eventService.updatePrice(eventId, priceId, request)));
    }

    @DeleteMapping("/{eventId}/prices/{priceId}")
    public ResponseEntity<ApiResponse<Void>> deletePrice(
            @PathVariable Long eventId, @PathVariable Long priceId) {
        eventService.deletePrice(eventId, priceId);
        return ResponseEntity.ok(ApiResponse.ok("Precio eliminado", null));
    }

    // ── Participaciones ───────────────────────────────────────────────────────

    @GetMapping("/{eventId}/participations")
    public ResponseEntity<ApiResponse<List<ParticipationResponse>>> getParticipations(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.findParticipationsByEventId(eventId)));
    }

    @PostMapping("/participations")
    public ResponseEntity<ApiResponse<ParticipationResponse>> addParticipation(
            @Valid @RequestBody ParticipationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Participación registrada", eventService.addParticipation(request)));
    }

    @DeleteMapping("/participations/{participationId}")
    public ResponseEntity<ApiResponse<Void>> removeParticipation(@PathVariable Long participationId) {
        eventService.removeParticipation(participationId);
        return ResponseEntity.ok(ApiResponse.ok("Alumno desapuntado del evento", null));
    }
}
