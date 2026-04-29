package com.dance.me.school.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String imageUrl;
    private UUID userId;
    private Boolean active;
    private LocalDateTime createdAt;
}
