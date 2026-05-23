package com.dance.me.coreografia.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

// Vista ligera para listados — sin escenas ni posiciones
@Data
@Builder
public class CoreografiaListResponse {

    private Long id;
    private Long schoolId;
    private String publicUrl;
    private String nombre;
    private String audioNombre;
    private Integer totalBailarines;
    private Integer totalEscenas;
    private LocalDateTime updatedAt;
}
