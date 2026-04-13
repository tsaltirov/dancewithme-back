package com.dance.me.costume.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostumeRequest {

    @NotNull(message = "La participación es obligatoria")
    private Long participationId;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    private String observations;
}
