package com.dance.me.coreografia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

// Documento completo — usado en GET /{id}, POST y PUT
@Data
@Builder
public class CoreografiaResponse {

    private Long id;
    private Long schoolId;
    private String publicUrl;
    private String nombre;
    private String audioUrl;
    private String audioNombre;
    private BigDecimal stageWidth;
    private BigDecimal stageDepth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BailarinDto> bailarines;
    private List<EscenaDto> escenas;
}
