package com.dance.me.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dance.me.event.entity.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;
    private BigDecimal price;
    private Integer maxCapacity;
    private Long schoolId;
    private String schoolName;
    private EventStatus status;
    private LocalDateTime createdAt;
}
