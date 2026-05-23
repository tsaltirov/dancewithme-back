package com.dance.me.coreografia.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EscenaPosicionDto {

    @NotNull
    private Long alumnoId;

    @NotNull
    private BigDecimal x;

    @NotNull
    private BigDecimal z;

    private BigDecimal ry = BigDecimal.ZERO;
}
