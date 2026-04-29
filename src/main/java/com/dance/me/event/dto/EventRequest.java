package com.dance.me.event.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventRequest {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String venue;

    private Integer maxCapacity;

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;
}
