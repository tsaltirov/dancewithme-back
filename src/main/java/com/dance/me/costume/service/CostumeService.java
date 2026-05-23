package com.dance.me.costume.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.costume.dto.CostumeRequest;
import com.dance.me.costume.dto.CostumeResponse;
import com.dance.me.costume.dto.EventCostumeRequest;
import com.dance.me.costume.dto.EventCostumeResponse;
import com.dance.me.costume.entity.Costume;
import com.dance.me.costume.entity.CostumeStatus;
import com.dance.me.costume.entity.EventCostume;
import com.dance.me.costume.mapper.CostumeMapper;
import com.dance.me.costume.repository.CostumeRepository;
import com.dance.me.costume.repository.EventCostumeRepository;
import com.dance.me.event.entity.EventParticipation;
import com.dance.me.event.repository.EventParticipationRepository;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CostumeService {

    private final CostumeRepository costumeRepository;
    private final EventCostumeRepository eventCostumeRepository;
    private final EventParticipationRepository participationRepository;
    private final SchoolRepository schoolRepository;
    private final CostumeMapper costumeMapper;

    // ── Catálogo CRUD ─────────────────────────────────────────────────────────

    public List<CostumeResponse> findBySchool(Long schoolId, Boolean active) {
        List<Costume> results;
        if (active == null) {
            results = costumeRepository.findBySchoolId(schoolId);
        } else if (active) {
            results = costumeRepository.findBySchoolIdAndActiveTrue(schoolId);
        } else {
            results = costumeRepository.findBySchoolIdAndActiveFalse(schoolId);
        }
        return results.stream().map(costumeMapper::toResponse).toList();
    }

    public CostumeResponse findById(Long id) {
        return costumeMapper.toResponse(getCostume(id));
    }

    @Transactional
    public CostumeResponse create(CostumeRequest request) {
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));

        if (costumeRepository.existsByNameAndSchoolId(request.getName(), request.getSchoolId())) {
            throw new BadRequestException("Ya existe un vestuario con ese nombre en esta escuela", "COSTUME_DUPLICATE_NAME");
        }

        Costume costume = Costume.builder()
                .school(school)
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .notes(request.getNotes())
                .quantity(request.getQuantity())
                .build();

        return costumeMapper.toResponse(costumeRepository.save(costume));
    }

    @Transactional
    public CostumeResponse update(Long id, CostumeRequest request) {
        Costume costume = getCostume(id);

        costume.setName(request.getName());
        costume.setDescription(request.getDescription());
        costume.setImageUrl(request.getImageUrl());
        costume.setNotes(request.getNotes());
        costume.setQuantity(request.getQuantity());

        return costumeMapper.toResponse(costumeRepository.save(costume));
    }

    @Transactional
    public void deactivate(Long id) {
        Costume costume = getCostume(id);
        costume.setActive(false);
        costumeRepository.save(costume);
    }

    @Transactional
    public CostumeResponse activate(Long id) {
        Costume costume = costumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Costume", id));
        costume.setActive(true);
        return costumeMapper.toResponse(costumeRepository.save(costume));
    }

    // ── Asignaciones a eventos ────────────────────────────────────────────────

    public List<EventCostumeResponse> findAssignmentsByParticipation(Long participationId) {
        return eventCostumeRepository.findByParticipationId(participationId).stream()
                .map(costumeMapper::toEventResponse)
                .toList();
    }

    public List<EventCostumeResponse> findPendingReturn() {
        return eventCostumeRepository
                .findByStatusIn(List.of(CostumeStatus.ENTREGADO, CostumeStatus.PENDIENTE_DEVOLUCION))
                .stream()
                .map(costumeMapper::toEventResponse)
                .toList();
    }

    @Transactional
    public EventCostumeResponse assign(EventCostumeRequest request) {
        EventParticipation participation = participationRepository
                .findById(request.getParticipationId())
                .orElseThrow(() -> new ResourceNotFoundException("Participation", request.getParticipationId()));

        Costume costume = getCostume(request.getCostumeId());

        if (costume.getQuantity() != null) {
            long inUse = eventCostumeRepository.countByCostumeIdAndStatusNot(
                    costume.getId(), CostumeStatus.DEVUELTO);
            if (inUse >= costume.getQuantity()) {
                throw new BadRequestException(
                        "No hay unidades disponibles de \"" + costume.getName() + "\"",
                        "COSTUME_NO_STOCK");
            }
        }

        EventCostume assignment = EventCostume.builder()
                .participation(participation)
                .costume(costume)
                .observations(request.getObservations())
                .build();

        return costumeMapper.toEventResponse(eventCostumeRepository.save(assignment));
    }

    @Transactional
    public void deleteAssignment(Long assignmentId) {
        EventCostume ec = eventCostumeRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("EventCostume", assignmentId));

        if (ec.getStatus() != CostumeStatus.ENTREGADO) {
            throw new BadRequestException(
                    "Solo se puede deshacer una asignación en estado ENTREGADO. Estado actual: " + ec.getStatus(),
                    "COSTUME_INVALID_STATUS");
        }

        eventCostumeRepository.delete(ec);
    }

    @Transactional
    public EventCostumeResponse markAsReturned(Long assignmentId) {
        EventCostume ec = eventCostumeRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("EventCostume", assignmentId));
        ec.setStatus(CostumeStatus.DEVUELTO);
        ec.setReturnDate(LocalDate.now());
        return costumeMapper.toEventResponse(eventCostumeRepository.save(ec));
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Costume getCostume(Long id) {
        return costumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Costume", id));
    }
}
