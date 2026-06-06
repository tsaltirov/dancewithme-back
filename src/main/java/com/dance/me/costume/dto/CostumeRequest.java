package com.dance.me.costume.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// Catálogo: crear / actualizar un vestuario de la escuela
@Data
public class CostumeRequest {

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;
    private String imageUrl;
    private String notes;
    private Integer quantity;
}
