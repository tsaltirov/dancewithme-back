package com.dance.me.costume.dto;

import java.time.LocalDate;

import com.dance.me.costume.entity.CostumeStatus;

import lombok.Builder;
import lombok.Data;

// Asignación: vestuario asignado a una participación de evento
@Data
@Builder
public class EventCostumeResponse {

    private Long id;
    private Long participationId;
    private String studentName;
    private String eventTitle;
    private Long costumeId;
    private String costumeName;
    private String costumeImageUrl;
    private CostumeStatus status;
    private LocalDate deliveryDate;
    private LocalDate returnDate;
    private String observations;
}
