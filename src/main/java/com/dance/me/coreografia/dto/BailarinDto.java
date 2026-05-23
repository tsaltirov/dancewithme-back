package com.dance.me.coreografia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BailarinDto {

    @NotNull
    private Long alumnoId;

    @NotNull
    private Integer color;

    @NotNull
    private Short orden;
}
