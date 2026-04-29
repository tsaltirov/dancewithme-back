package com.dance.me.costume.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// Asignación: asignar un vestuario del catálogo a una participación de evento
@Data
public class EventCostumeRequest {

    @NotNull(message = "La participación es obligatoria")
    private Long participationId;

    @NotNull(message = "El vestuario es obligatorio")
    private Long costumeId;

    private String observations;
}
