package com.dance.me.event.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dance.me.common.exception.BadRequestException;
import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.event.dto.EventRequest;
import com.dance.me.event.dto.EventResponse;
import com.dance.me.event.dto.ParticipationRequest;
import com.dance.me.event.dto.ParticipationResponse;
import com.dance.me.event.entity.Event;
import com.dance.me.event.entity.EventParticipation;
import com.dance.me.event.mapper.EventMapper;
import com.dance.me.event.repository.EventParticipationRepository;
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
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final EventMapper eventMapper;

    public List<EventResponse> findBySchoolId(Long schoolId) {
        return eventRepository.findBySchoolId(schoolId).stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public EventResponse findById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id));
    }

    @Transactional
    public EventResponse create(EventRequest request) {
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("School", request.getSchoolId()));
        Event event = eventMapper.toEntity(request, school);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse update(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id));
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setVenue(request.getVenue());
        event.setPrice(request.getPrice());
        event.setMaxCapacity(request.getMaxCapacity());
        return eventMapper.toResponse(eventRepository.save(event));
    }

    public List<ParticipationResponse> findParticipationsByEventId(Long eventId) {
        return participationRepository.findByEventId(eventId).stream()
                .map(eventMapper::toParticipationResponse)
                .toList();
    }

    @Transactional
    public ParticipationResponse addParticipation(ParticipationRequest request) {
        if (participationRepository.existsByEventIdAndStudentId(request.getEventId(), request.getStudentId())) {
            throw new BadRequestException("El alumno ya participa en este evento");
        }
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event", request.getEventId()));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));
        EventParticipation participation = EventParticipation.builder()
                .event(event)
                .student(student)
                .build();
        return eventMapper.toParticipationResponse(participationRepository.save(participation));
    }
}
