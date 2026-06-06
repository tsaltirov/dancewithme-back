package com.dance.me.school.dto;

import java.util.UUID;

import com.dance.me.school.entity.SchoolRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolMemberResponse {

    private UUID userId;
    private String name;
    private String lastName;
    private String email;
    private String imageUrl;
    private SchoolRole role;
    private Boolean active;
}
