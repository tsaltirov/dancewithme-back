package com.dance.me.costume.mapper;

import org.springframework.stereotype.Component;

import com.dance.me.costume.dto.CostumeResponse;
import com.dance.me.costume.dto.EventCostumeResponse;
import com.dance.me.costume.entity.Costume;
import com.dance.me.costume.entity.EventCostume;
import com.dance.me.event.entity.EventParticipation;

@Component
public class CostumeMapper {

    public CostumeResponse toResponse(Costume costume) {
        return CostumeResponse.builder()
                .id(costume.getId())
                .schoolId(costume.getSchool().getId())
                .name(costume.getName())
                .description(costume.getDescription())
                .imageUrl(costume.getImageUrl())
                .notes(costume.getNotes())
                .quantity(costume.getQuantity())
                .active(costume.getActive())
                .createdAt(costume.getCreatedAt())
                .build();
    }

    public EventCostumeResponse toEventResponse(EventCostume ec) {
        EventParticipation p = ec.getParticipation();
        Costume c = ec.getCostume();
        return EventCostumeResponse.builder()
                .id(ec.getId())
                .participationId(p.getId())
                .studentName(p.getStudent().getName() + " " + p.getStudent().getLastName())
                .eventTitle(p.getEvent().getTitle())
                .costumeId(c.getId())
                .costumeName(c.getName())
                .costumeImageUrl(c.getImageUrl())
                .status(ec.getStatus())
                .deliveryDate(ec.getDeliveryDate())
                .returnDate(ec.getReturnDate())
                .observations(ec.getObservations())
                .build();
    }
}
