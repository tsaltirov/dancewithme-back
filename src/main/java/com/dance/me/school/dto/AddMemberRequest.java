package com.dance.me.school.dto;

import java.util.UUID;

import com.dance.me.school.entity.SchoolRole;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddMemberRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private SchoolRole role;
}
