package com.dance.me.school.dto;

import java.time.LocalDateTime;

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
    private Long userId;
    private Boolean active;
    private LocalDateTime createdAt;
}
