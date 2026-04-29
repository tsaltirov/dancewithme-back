package com.dance.me.costume.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoxRequest {

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;
}
