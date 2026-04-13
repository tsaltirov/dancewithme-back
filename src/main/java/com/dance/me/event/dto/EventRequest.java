package com.dance.me.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String venue;

    @PositiveOrZero(message = "El precio no puede ser negativo")
    private BigDecimal price;

    private Integer maxCapacity;

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;
}
