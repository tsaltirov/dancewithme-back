package com.dance.me.event.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.event.dto.EventPriceRequest;
import com.dance.me.event.dto.EventPriceResponse;
import com.dance.me.event.dto.EventRequest;
import com.dance.me.event.dto.EventResponse;
import com.dance.me.event.dto.ParticipationRequest;
import com.dance.me.event.dto.ParticipationResponse;
import com.dance.me.costume.entity.CostumeStatus;
import com.dance.me.costume.repository.EventCostumeRepository;
import com.dance.me.event.entity.Event;
import com.dance.me.event.entity.EventParticipation;
import com.dance.me.event.entity.EventPrice;
import com.dance.me.event.entity.EventStatus;
import com.dance.me.event.mapper.EventMapper;
import com.dance.me.event.repository.EventParticipationRepository;
import com.dance.me.event.repository.EventPriceRepository;
import com.dance.me.event.repository.EventRepository;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;
import com.dance.me.student.entity.Student;
import com.dance.me.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final EventParticipationRepository participationRepository;
    private final EventPriceRepository priceRepository;
    private final EventCostumeRepository eventCostumeRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final EventMapper eventMapper;

    // ── Eventos ───────────────────────────────────────────────────────────────

    public List<EventResponse> findBySchoolId(Long schoolId) {
        return eventRepository.findBySchoolId(schoolId).stream()
                .map(e -> eventMapper.toResponse(e, priceRepository.findByEventId(e.getId())))
                .toList();
    }

    public EventResponse findById(Long id) {
        Event event = getEvent(id);
        return eventMapper.toResponse(event, priceRepository.findByEventId(id));
    }

    @Transactional
    public EventResponse create(EventRequest request) {
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));
        Event event = eventRepository.save(eventMapper.toEntity(request, school));
        return eventMapper.toResponse(event, List.of());
    }

    @Transactional
    public EventResponse update(Long id, EventRequest request) {
        Event event = getEvent(id);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setVenue(request.getVenue());
        event.setMaxCapacity(request.getMaxCapacity());
        event = eventRepository.save(event);
        return eventMapper.toResponse(event, priceRepository.findByEventId(id));
    }

    // ── Precios ───────────────────────────────────────────────────────────────

    public List<EventPriceResponse> findPrices(Long eventId) {
        return priceRepository.findByEventId(eventId).stream()
                .map(eventMapper::toPriceResponse)
                .toList();
    }

    @Transactional
    public EventPriceResponse addPrice(Long eventId, EventPriceRequest request) {
        Event event = getEvent(eventId);
        EventPrice price = EventPrice.builder()
                .event(event)
                .type(request.getType().toUpperCase())
                .label(request.getLabel())
                .amount(request.getAmount())
                .optional(request.getOptional() != null && request.getOptional())
                .build();
        return eventMapper.toPriceResponse(priceRepository.save(price));
    }

    @Transactional
    public EventPriceResponse updatePrice(Long eventId, Long priceId, EventPriceRequest request) {
        EventPrice price = priceRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("EventPrice", priceId));
        if (!price.getEvent().getId().equals(eventId)) {
            throw new BadRequestException("El precio no pertenece a este evento");
        }
        price.setType(request.getType().toUpperCase());
        price.setLabel(request.getLabel());
        price.setAmount(request.getAmount());
        price.setOptional(request.getOptional() != null && request.getOptional());
        return eventMapper.toPriceResponse(priceRepository.save(price));
    }

    @Transactional
    public void deletePrice(Long eventId, Long priceId) {
        EventPrice price = priceRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("EventPrice", priceId));
        if (!price.getEvent().getId().equals(eventId)) {
            throw new BadRequestException("El precio no pertenece a este evento");
        }
        priceRepository.delete(price);
    }

    // ── Estado ───────────────────────────────────────────────────────────────

    @Transactional
    public EventResponse cancel(Long id) {
        Event event = getEvent(id);
        if (event.getStatus() == EventStatus.CANCELADO) {
            throw new BadRequestException("El evento ya está cancelado");
        }
        event.setStatus(EventStatus.CANCELADO);
        event = eventRepository.save(event);
        return eventMapper.toResponse(event, priceRepository.findByEventId(id));
    }

    // ── Participaciones ───────────────────────────────────────────────────────

    public List<ParticipationResponse> findParticipationsByEventId(Long eventId) {
        return participationRepository.findByEventId(eventId).stream()
                .map(eventMapper::toParticipationResponse)
                .toList();
    }

    @Transactional
    public void removeParticipation(Long participationId) {
        EventParticipation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation", participationId));

        boolean hasPendingCostumes = eventCostumeRepository
                .findByParticipationId(participationId).stream()
                .anyMatch(ec -> ec.getStatus() != CostumeStatus.DEVUELTO);

        if (hasPendingCostumes) {
            throw new BadRequestException(
                    "El alumno tiene vestuario pendiente de devolución. Recupéralo antes de desapuntarlo.");
        }

        // Borrar primero los costumes ya devueltos (FK) y luego la participación
        eventCostumeRepository.deleteAll(
                eventCostumeRepository.findByParticipationId(participationId));
        participationRepository.delete(participation);
    }

    @Transactional
    public ParticipationResponse addParticipation(ParticipationRequest request) {
        if (participationRepository.existsByEventIdAndStudentId(request.getEventId(), request.getStudentId())) {
            throw new BadRequestException("El alumno ya participa en este evento");
        }
        Event event = getEvent(request.getEventId());
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));
        return eventMapper.toParticipationResponse(
                participationRepository.save(EventParticipation.builder()
                        .event(event)
                        .student(student)
                        .build()));
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id));
    }
}
