package com.dance.me.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String danceStyle;

    private String level;

    @Positive(message = "La capacidad debe ser positiva")
    private Integer maxCapacity;

    private String schedule;

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;
}
