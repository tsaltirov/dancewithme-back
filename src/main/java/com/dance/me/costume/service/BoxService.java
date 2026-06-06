package com.dance.me.costume.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.costume.dto.BoxRequest;
import com.dance.me.costume.dto.BoxResponse;
import com.dance.me.costume.dto.CostumeResponse;
import com.dance.me.costume.entity.Box;
import com.dance.me.costume.entity.BoxCostume;
import com.dance.me.costume.entity.Costume;
import com.dance.me.costume.entity.EventBox;
import com.dance.me.costume.mapper.CostumeMapper;
import com.dance.me.costume.repository.BoxCostumeRepository;
import com.dance.me.costume.repository.BoxRepository;
import com.dance.me.costume.repository.CostumeRepository;
import com.dance.me.costume.repository.EventBoxRepository;
import com.dance.me.event.entity.Event;
import com.dance.me.event.repository.EventRepository;
import com.dance.me.school.entity.School;
import com.dance.me.school.repository.SchoolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoxService {

    private final BoxRepository boxRepository;
    private final BoxCostumeRepository boxCostumeRepository;
    private final EventBoxRepository eventBoxRepository;
    private final CostumeRepository costumeRepository;
    private final EventRepository eventRepository;
    private final SchoolRepository schoolRepository;
    private final CostumeMapper costumeMapper;

    // ── Catálogo de cajas ─────────────────────────────────────────────────────

    public List<BoxResponse> findBySchool(Long schoolId) {
        return boxRepository.findBySchoolIdAndActiveTrue(schoolId).stream()
                .map(this::toResponse)
                .toList();
    }

    public BoxResponse findById(Long id) {
        return toResponse(getBox(id));
    }

    @Transactional
    public BoxResponse create(BoxRequest request) {
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));

        Box box = Box.builder()
                .school(school)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return toResponse(boxRepository.save(box));
    }

    @Transactional
    public BoxResponse update(Long id, BoxRequest request) {
        Box box = getBox(id);
        box.setName(request.getName());
        box.setDescription(request.getDescription());
        return toResponse(boxRepository.save(box));
    }

    @Transactional
    public void deactivate(Long id) {
        Box box = getBox(id);
        box.setActive(false);
        boxRepository.save(box);
    }

    // ── Costumes dentro de una caja ───────────────────────────────────────────

    @Transactional
    public BoxResponse addCostume(Long boxId, Long costumeId) {
        if (boxCostumeRepository.existsByBoxIdAndCostumeId(boxId, costumeId)) {
            throw new BadRequestException("Este vestuario ya está en la caja");
        }
        Box box = getBox(boxId);
        Costume costume = costumeRepository.findById(costumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Costume", costumeId));

        boxCostumeRepository.save(BoxCostume.builder().box(box).costume(costume).build());
        return toResponse(box);
    }

    @Transactional
    public void removeCostume(Long boxId, Long costumeId) {
        BoxCostume bc = boxCostumeRepository.findByBoxIdAndCostumeId(boxId, costumeId)
                .orElseThrow(() -> new ResourceNotFoundException("BoxCostume", costumeId));
        boxCostumeRepository.delete(bc);
    }

    // ── Cajas asignadas a eventos ─────────────────────────────────────────────

    public List<BoxResponse> findByEvent(Long eventId) {
        return eventBoxRepository.findByEventId(eventId).stream()
                .map(eb -> toResponse(eb.getBox()))
                .toList();
    }

    @Transactional
    public void assignToEvent(Long boxId, Long eventId) {
        if (eventBoxRepository.existsByEventIdAndBoxId(eventId, boxId)) {
            throw new BadRequestException("Esta caja ya está asignada al evento");
        }
        Box box = getBox(boxId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", eventId));

        eventBoxRepository.save(EventBox.builder().event(event).box(box).build());
    }

    @Transactional
    public void removeFromEvent(Long boxId, Long eventId) {
        EventBox eb = eventBoxRepository.findByEventIdAndBoxId(eventId, boxId)
                .orElseThrow(() -> new ResourceNotFoundException("EventBox", boxId));
        eventBoxRepository.delete(eb);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Box getBox(Long id) {
        return boxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Box", id));
    }

    private BoxResponse toResponse(Box box) {
        List<CostumeResponse> costumes = boxCostumeRepository.findByBoxId(box.getId()).stream()
                .map(bc -> costumeMapper.toResponse(bc.getCostume()))
                .toList();

        return BoxResponse.builder()
                .id(box.getId())
                .schoolId(box.getSchool().getId())
                .name(box.getName())
                .description(box.getDescription())
                .active(box.getActive())
                .createdAt(box.getCreatedAt())
                .costumes(costumes)
                .build();
    }
}
