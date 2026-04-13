package com.dance.me.costume.mapper;

import org.springframework.stereotype.Component;

import com.dance.me.costume.dto.CostumeResponse;
import com.dance.me.costume.entity.EventCostume;
import com.dance.me.event.entity.EventParticipation;

@Component
public class CostumeMapper {

    public CostumeResponse toResponse(EventCostume costume) {
        EventParticipation p = costume.getParticipation();
        return CostumeResponse.builder()
                .id(costume.getId())
                .participationId(p.getId())
                .studentName(p.getStudent().getName() + " " + p.getStudent().getLastName())
                .eventTitle(p.getEvent().getTitle())
                .description(costume.getDescription())
                .status(costume.getStatus())
                .deliveryDate(costume.getDeliveryDate())
                .returnDate(costume.getReturnDate())
                .observations(costume.getObservations())
                .build();
    }

    public EventCostume toEntity(EventParticipation participation, String description, String observations) {
        return EventCostume.builder()
                .participation(participation)
                .description(description)
                .observations(observations)
                .build();
    }
}
