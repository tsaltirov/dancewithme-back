package com.dance.me.event.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {

    @NotNull(message = "El evento es obligatorio")
    private Long eventId;

    @NotNull(message = "El alumno es obligatorio")
    private Long studentId;
}
