package com.dance.me.group.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {

    private Long id;
    private String name;
    private String danceStyle;
    private String level;
    private Integer maxCapacity;
    private String schedule;
    private Long schoolId;
    private String schoolName;
    private Boolean active;
    private LocalDateTime createdAt;
}
