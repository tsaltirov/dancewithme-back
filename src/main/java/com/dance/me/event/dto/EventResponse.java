package com.dance.me.event.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dance.me.event.entity.EventStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;
    private Integer maxCapacity;
    private Long schoolId;
    private String schoolName;
    private EventStatus status;
    private LocalDateTime createdAt;
    private List<EventPriceResponse> prices;
}
