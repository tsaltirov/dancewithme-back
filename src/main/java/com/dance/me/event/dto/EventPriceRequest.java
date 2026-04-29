package com.dance.me.event.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class EventPriceRequest {

    @NotBlank(message = "El tipo de precio es obligatorio")
    private String type;

    private String label;

    @NotNull(message = "El importe es obligatorio")
    @PositiveOrZero(message = "El importe no puede ser negativo")
    private BigDecimal amount;

    private Boolean optional = false;
}
