package com.dance.me.costume.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoxResponse {

    private Long id;
    private Long schoolId;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private List<CostumeResponse> costumes;
}
