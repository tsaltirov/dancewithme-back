package com.dance.me.costume.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

// Catálogo: respuesta de un vestuario
@Data
@Builder
public class CostumeResponse {

    private Long id;
    private Long schoolId;
    private String name;
    private String description;
    private String imageUrl;
    private String notes;
    private Integer quantity;
    private Boolean active;
    private LocalDateTime createdAt;
}
