package com.dance.me.event.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dance.me.event.dto.EventPriceResponse;
import com.dance.me.event.dto.EventRequest;
import com.dance.me.event.dto.EventResponse;
import com.dance.me.event.dto.ParticipationResponse;
import com.dance.me.event.entity.Event;
import com.dance.me.event.entity.EventParticipation;
import com.dance.me.event.entity.EventPrice;
import com.dance.me.school.entity.School;
import com.dance.me.student.entity.Student;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event, List<EventPrice> prices) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .venue(event.getVenue())
                .maxCapacity(event.getMaxCapacity())
                .schoolId(event.getSchool().getId())
                .schoolName(event.getSchool().getName())
                .status(event.getStatus())
                .createdAt(event.getCreatedAt())
                .prices(prices.stream().map(this::toPriceResponse).toList())
                .build();
    }

    public EventPriceResponse toPriceResponse(EventPrice p) {
        return EventPriceResponse.builder()
                .id(p.getId())
                .type(p.getType())
                .label(p.getLabel())
                .amount(p.getAmount())
                .optional(p.getOptional())
                .build();
    }

    public Event toEntity(EventRequest request, School school) {
        return Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .venue(request.getVenue())
                .maxCapacity(request.getMaxCapacity())
                .school(school)
                .build();
    }

    public ParticipationResponse toParticipationResponse(EventParticipation p) {
        Student student = p.getStudent();
        Event event = p.getEvent();
        return ParticipationResponse.builder()
                .id(p.getId())
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .studentId(student.getId())
                .studentName(student.getName() + " " + student.getLastName())
                .registrationDate(p.getRegistrationDate())
                .paymentStatus(p.getPaymentStatus())
                .paidAmount(p.getPaidAmount())
                .build();
    }
}
