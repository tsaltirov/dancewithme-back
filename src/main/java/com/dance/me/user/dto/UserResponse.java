package com.dance.me.user.dto;

import java.time.LocalDateTime;

import com.dance.me.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;
}
