package com.dance.me.coreografia.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoreografiaRequest {

    @NotNull(message = "La escuela es obligatoria")
    private Long schoolId;

    private String nombre;

    private String audioUrl;
    private String audioNombre;

    private BigDecimal stageWidth;
    private BigDecimal stageDepth;

    @NotNull
    @Valid
    private List<BailarinDto> bailarines;

    @NotNull
    @Valid
    private List<EscenaDto> escenas;
}
