package com.dance.me.event.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventPriceResponse {

    private Long id;
    private String type;
    private String label;
    private BigDecimal amount;
    private Boolean optional;
}
