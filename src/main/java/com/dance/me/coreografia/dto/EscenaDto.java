package com.dance.me.coreografia.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EscenaDto {

    private Long id;

    @NotBlank
    private String nombre;

    @NotNull
    private Short orden;

    @NotNull
    private BigDecimal duracion;

    private BigDecimal holdRatio = new BigDecimal("0.250");

    private String easing = "linear";

    private String videoUrl;
    private String videoTipo;
    private String videoEmbed;

    @NotNull
    private List<EscenaPosicionDto> posiciones;
}
