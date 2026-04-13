package com.dance.me.costume.dto;

import java.time.LocalDate;

import com.dance.me.costume.entity.CostumeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostumeResponse {

    private Long id;
    private Long participationId;
    private String studentName;
    private String eventTitle;
    private String description;
    private CostumeStatus status;
    private LocalDate deliveryDate;
    private LocalDate returnDate;
    private String observations;
}
